package com.fast.rpc.rpc;

import com.fast.rpc.cluster.Cluster;
import com.fast.rpc.common.URL;
import com.fast.rpc.common.URLParam;
import com.fast.rpc.core.ExtensionLoader;
import com.google.common.collect.ArrayListMultimap;

import java.util.List;

/**
 * @ClassName DefaultConfigHandler
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/26 23:30
 * @Version 1.0
 **/
public class DefaultConfigHandler implements ConfigHandler {

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
        Provider<T> provider = new DefaultProvider<T>(ref, serviceUrl, interfaceClass);

        Protocol protocol = new ProtocolFilterWrapper(ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(protocolName));
        Exporter<T> exporter = protocol.export(provider, serviceUrl);

        // register service
        //register(registryUrls, serviceUrl);

        return exporter;
    }

    @Override
    public <T> void unexport(List<Exporter<T>> exporters, ArrayListMultimap<URL, URL> registryUrls) {

    }
}
