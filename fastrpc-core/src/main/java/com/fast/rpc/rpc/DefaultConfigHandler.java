package com.fast.rpc.rpc;

import com.fast.rpc.cluster.Cluster;
import com.fast.rpc.common.URL;
import com.fast.rpc.common.URLParam;
import com.fast.rpc.core.ExtensionLoader;
import com.fast.rpc.exception.RpcFrameworkException;
import com.fast.rpc.registry.Registry;
import com.fast.rpc.registry.RegistryFactory;
import com.google.common.collect.ArrayListMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @ClassName DefaultConfigHandler
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/26 23:30
 * @Version 1.0
 **/
public class DefaultConfigHandler implements ConfigHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public <T> Cluster<T> buildCluster(Class<T> interfaceClass, URL refUrl, List<URL> registryUrls) {
        return null;
    }

    @Override
    public <T> T refer(Class<T> interfaceClass, List<Cluster<T>> cluster, String proxyType) {
        return null;
    }

    @Override
    public <T> Exporter<T> export(Class<T> interfaceClass, T ref, URL serviceUrl, List<URL> registryUrls) {
        String protocolName = serviceUrl.getParameter(URLParam.protocol.getName(), URLParam.protocol.getValue());
        Provider<T> provider = new DefaultProvider(ref, serviceUrl, interfaceClass);

        Protocol protocol = new ProtocolFilterWrapper(ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(protocolName));
        Exporter<T> exporter = protocol.export(provider, serviceUrl);

        // register service
        register(registryUrls, serviceUrl);

        return exporter;
    }

    private void register(List<URL> registryUrls, URL serviceUrl) {
        registryUrls.stream().forEach(registryUrl -> {
            // 根据check参数的设置，register失败可能会抛异常，上层应该知晓
            RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getExtension(registryUrl.getProtocol());
            if (registryFactory == null) {
                throw new RpcFrameworkException("register error! Could not find extension for registry protocol:" + serviceUrl.getProtocol()
                        + ", make sure registry module for " + registryUrl.getProtocol() + " is in classpath!");
            }

            try {
                Registry registry = registryFactory.getRegistry(registryUrl);
                registry.register(serviceUrl);
            } catch (Exception e) {
                throw new RpcFrameworkException("register error! Could not registry service:" + registryUrl.getInterfaceName()
                        + " for " + registryUrl.getProtocol(), e);
            }
        });
    }

    @Override
    public <T> void unexport(List<Exporter<T>> exporters, ArrayListMultimap<URL, URL> registryUrls) {
        unRegister(registryUrls);
    }

    private void unRegister(ArrayListMultimap<URL, URL> registryUrls) {
        for (URL serviceUrl : registryUrls.keySet()) {
            for (URL url : registryUrls.get(serviceUrl)) {
                try {
                    RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getExtension(url.getProtocol());
                    Registry registry = registryFactory.getRegistry(url);
                    registry.unregister(serviceUrl);
                } catch (Exception e) {
                    logger.warn(String.format("unregister url false:%s", url), e);
                }
            }
        }
    }

}
