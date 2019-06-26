package com.fast.rpc.rpc;

import com.fast.rpc.common.URL;

/**
 * @ClassName DefaultRpcProtocol
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/26 23:56
 * @Version 1.0
 **/
public class DefaultRpcProtocol extends AbstractProtocol {

    @Override
    protected <T> Reference<T> createReference(Class<T> clz, URL url, URL serviceUrl) {
        return null;
    }

    @Override
    protected <T> Exporter<T> createExporter(Provider<T> provider, URL url) {
        return null;
    }

    @Override
    public void destroy() {

    }
}
