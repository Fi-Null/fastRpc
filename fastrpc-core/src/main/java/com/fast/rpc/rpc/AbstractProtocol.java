package com.fast.rpc.rpc;

import com.fast.rpc.common.URL;
import com.fast.rpc.exception.RpcFrameworkException;
import com.fast.rpc.util.FrameworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName AbstractProtocol
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/26 23:48
 * @Version 1.0
 **/
public abstract class AbstractProtocol implements Protocol {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected ConcurrentHashMap<String, Exporter<?>> exporterMap = new ConcurrentHashMap<>();

    @Override
    public <T> Reference<T> refer(Class<T> clz, URL url, URL serviceUrl) {
        return null;
    }

    @Override
    public <T> Exporter<T> export(Provider<T> provider, URL url) {
        if (url == null) {
            throw new RpcFrameworkException(this.getClass().getSimpleName() + " export Error: url is null");
        }

        if (provider == null) {
            throw new RpcFrameworkException(this.getClass().getSimpleName() + " export Error: provider is null, url=" + url);
        }

        String protocolKey = FrameworkUtils.getProtocolKey(url);

        synchronized (exporterMap) {
            Exporter<T> exporter = (Exporter<T>) exporterMap.get(protocolKey);

            if (exporter != null) {
                throw new RpcFrameworkException(this.getClass().getSimpleName() + " export Error: service already exist, url=" + url);
            }

            exporter = createExporter(provider, url);
            exporter.init();
            exporterMap.put(protocolKey, exporter);
            logger.info(this.getClass().getSimpleName() + " export success: url=" + url);
            return exporter;
        }
    }

    protected abstract <T> Reference<T> createReference(Class<T> clz, URL url, URL serviceUrl);

    protected abstract <T> Exporter<T> createExporter(Provider<T> provider, URL url);
}
