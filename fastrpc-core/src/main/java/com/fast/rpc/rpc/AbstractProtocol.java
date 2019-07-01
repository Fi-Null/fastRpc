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
    public <T> Reference<T> refer(Class<T> clz, URL referenceURL, URL url) {
        if (referenceURL == null) {
            throw new RpcFrameworkException(this.getClass().getSimpleName() + " refer Error: url is null");
        }
        if (clz == null) {
            throw new RpcFrameworkException(this.getClass().getSimpleName() + " refer Error: class is null, url=" + url);
        }
        Reference<T> reference = createReference(clz, referenceURL, url);
        reference.init();

        logger.info(this.getClass().getSimpleName() + " refer service:{} success url:{}", clz.getName(), url);
        return reference;
    }

    @Override
    public <T> Exporter<T> export(Provider<T> provider, URL serviceUrl) {
        if (serviceUrl == null) {
            throw new RpcFrameworkException(this.getClass().getSimpleName() + " export Error: url is null");
        }

        if (provider == null) {
            throw new RpcFrameworkException(this.getClass().getSimpleName() + " export Error: provider is null, url=" + serviceUrl);
        }

        String protocolKey = FrameworkUtils.getProtocolKey(serviceUrl);

        synchronized (exporterMap) {
            Exporter<T> exporter = (Exporter<T>) exporterMap.get(protocolKey);

            if (exporter != null) {
                throw new RpcFrameworkException(this.getClass().getSimpleName() + " export Error: service already exist, url=" + serviceUrl);
            }

            exporter = createExporter(provider, serviceUrl);
            exporter.init();
            exporterMap.put(protocolKey, exporter);
            logger.info(this.getClass().getSimpleName() + " export success: url=" + serviceUrl);
            return exporter;
        }
    }

    protected abstract <T> Reference<T> createReference(Class<T> clz, URL url, URL serviceUrl);

    protected abstract <T> Exporter<T> createExporter(Provider<T> provider, URL serviceUrl);
}
