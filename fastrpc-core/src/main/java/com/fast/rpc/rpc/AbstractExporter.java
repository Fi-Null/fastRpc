package com.fast.rpc.rpc;

import com.fast.rpc.common.URL;

/**
 * @ClassName AbstractExporter
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 00:07
 * @Version 1.0
 **/
public abstract class AbstractExporter<T> implements Exporter<T> {

    protected Provider<T> provider;

    protected URL serviceUrl;

    public AbstractExporter(Provider<T> provider, URL serviceUrl) {
        this.serviceUrl = serviceUrl;
        this.provider = provider;
    }

    @Override
    public String desc() {
        return "[" + this.getClass().getName() + "] url=" + serviceUrl;
    }

    @Override
    public Provider<T> getProvider() {
        return provider;
    }

    @Override
    public URL getUrl() {
        return serviceUrl;
    }
}
