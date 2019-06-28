package com.fast.rpc.rpc;

import com.fast.rpc.common.URL;
import com.fast.rpc.transport.NettyServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName DefaultRpcProtocol
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/26 23:56
 * @Version 1.0
 **/
public class DefaultRpcProtocol extends AbstractProtocol {

    private final ConcurrentHashMap<String, NettyServer> ipPort2Server = new ConcurrentHashMap<>();
    // 多个service可能在相同端口进行服务暴露，因此来自同个端口的请求需要进行路由以找到相应的服务，同时不在该端口暴露的服务不应该被找到
    private final Map<String, MessageRouter> ipPort2RequestRouter = new HashMap();

    @Override
    protected <T> Reference<T> createReference(Class<T> clz, URL url, URL serviceUrl) {
        return null;
    }

    @Override
    protected <T> Exporter<T> createExporter(Provider<T> provider, URL serviceUrl) {
        return new DefaultRpcExporter<>(provider, serviceUrl, ipPort2Server);
    }

    @Override
    public void destroy() {

    }
}
