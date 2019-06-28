package com.fast.rpc.config;

import com.fast.rpc.common.Constants;
import com.fast.rpc.common.URL;
import com.fast.rpc.common.URLParam;
import com.fast.rpc.core.ExtensionLoader;
import com.fast.rpc.rpc.ConfigHandler;
import com.fast.rpc.rpc.Exporter;
import com.fast.rpc.util.CollectionUtil;
import com.fast.rpc.util.StringUtils;
import com.google.common.collect.ArrayListMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName ServiceConfig
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/23 23:18
 * @Version 1.0
 **/
public class ServiceConfig<T> extends AbstractInterfaceConfig {

    private static final long serialVersionUID = -6784362174923673740L;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private volatile boolean exported = false;

    private List<Exporter<T>> exporters = new CopyOnWriteArrayList();

    private ArrayListMultimap<URL, URL> registeredUrls = ArrayListMultimap.create();

    private Class<T> interfaceClass;

    private T ref;

    protected synchronized void export() {
        if (exported) {
            logger.warn(String.format("%s has already been exported, so ignore the export request!", interfaceName));
            return;
        }


        if (ref == null) {
            throw new IllegalStateException("ref not allow null!");
        }
        try {
            interfaceClass = (Class<T>) Class.forName(interfaceName, true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        if (!interfaceClass.isAssignableFrom(ref.getClass())) {
            throw new IllegalArgumentException(ref.getClass() + " is not " + interfaceClass + " sub class!");
        }

        if (getRegistries() == null || getRegistries().isEmpty()) {
            throw new IllegalStateException("Should set registry config for service:" + interfaceClass.getName());
        }

        List<URL> registryUrls = loadRegistryUrls();
        if (CollectionUtil.isEmpty(registryUrls)) {
            throw new IllegalStateException("Should set registry config for service:" + interfaceClass.getName());
        }

        protocols.stream().forEach(protocolConfig -> {
            doExport(protocolConfig, registryUrls);
        });

        exported = true;
    }

    private void doExport(ProtocolConfig protocolConfig, List<URL> registryUrls) {
        String protocolName = protocolConfig.getName();
        if (protocolName == null || protocolName.length() == 0) {
            protocolName = URLParam.protocol.getValue();
        }

        Integer port = getProtocolPort(protocolConfig);
        String hostAddress = getLocalHostAddress(protocolConfig);

        Map<String, String> map = new HashMap<String, String>();
        map.put(URLParam.version.getName(), StringUtils.isNotEmpty(version) ? version : URLParam.version.getValue());
        map.put(URLParam.group.getName(), StringUtils.isNotEmpty(group) ? group : URLParam.group.getValue());
        map.put(URLParam.serialization.getName(), StringUtils.isNotEmpty(protocolConfig.getSerialization()) ? protocolConfig.getSerialization() : URLParam.serialization.getValue());
        map.put(URLParam.requestTimeout.getName(), timeout != null ? timeout.toString() : URLParam.requestTimeout.getValue());
        map.put(URLParam.side.getName(), Constants.PROVIDER);
        map.put(URLParam.timestamp.getName(), String.valueOf(System.currentTimeMillis()));

        URL serviceUrl = new URL(protocolName, hostAddress, port, interfaceClass.getName(), map);

        for (URL ru : registryUrls) {
            registeredUrls.put(serviceUrl, ru);
        }

        ConfigHandler configHandler = ExtensionLoader.getExtensionLoader(ConfigHandler.class).getExtension(Constants.DEFAULT_VALUE);
        exporters.add(configHandler.export(interfaceClass, ref, serviceUrl, registryUrls));
    }


    public boolean isExported() {
        return exported;
    }

    protected void destroy0() throws Exception {
        if (!isExported()) {
            return;
        }

        ConfigHandler configHandler = ExtensionLoader.getExtensionLoader(ConfigHandler.class).getExtension(Constants.DEFAULT_VALUE);
        configHandler.unexport(exporters, registeredUrls);

        exporters.clear();
        registeredUrls.clear();
    }

}
