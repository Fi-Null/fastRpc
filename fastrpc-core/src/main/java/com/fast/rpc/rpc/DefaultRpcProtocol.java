package com.fast.rpc.rpc;

import com.fast.rpc.common.URL;
import com.fast.rpc.transport.NettyServer;
import com.fast.rpc.transport.NettyServerImpl;
import com.fast.rpc.util.FrameworkUtils;

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
        return new DefaultRpcExporter(provider, serviceUrl);
    }

    @Override
    public void destroy() {

    }

    class DefaultRpcExporter<T> extends AbstractExporter<T> {
        private NettyServer server;

        DefaultRpcExporter(Provider<T> provider, URL serviceUrl) {
            super(provider, serviceUrl);
            this.server = initServer(serviceUrl);
        }

        private NettyServer initServer(URL url) {
            String ipPort = url.getServerAndPort();
            MessageRouter router = initRequestRouter(url);
            NettyServer server;
            synchronized (ipPort2Server) {
                server = ipPort2Server.get(ipPort);
                if (server == null) {
                    server = new NettyServerImpl(serviceUrl, router);
                    ipPort2Server.put(ipPort, server);
                }
            }
            return server;
        }

        private MessageRouter initRequestRouter(URL serviceUrl) {
            MessageRouter requestRouter;
            String ipPort = serviceUrl.getServerAndPort();

            synchronized (ipPort2RequestRouter) {
                requestRouter = ipPort2RequestRouter.get(ipPort);

                if (requestRouter == null) {
                    requestRouter = new MessageRouter(provider);
                    ipPort2RequestRouter.put(ipPort, requestRouter);
                } else {
                    requestRouter.addProvider(provider);
                }
            }

            return requestRouter;
        }

        @Override
        public void unexport() {
            String protocolKey = FrameworkUtils.getProtocolKey(serviceUrl);
            String ipPort = serviceUrl.getServerAndPort();

            Exporter<T> exporter = (Exporter<T>) exporterMap.remove(protocolKey);

            if (exporter != null) {
                exporter.destroy();
            }

            synchronized (ipPort2RequestRouter) {
                MessageRouter requestRouter = ipPort2RequestRouter.get(ipPort);

                if (requestRouter != null) {
                    requestRouter.removeProvider(provider);
                }
            }

            logger.info("DefaultRpcExporter unexport success: serviceUrl={}", serviceUrl);
        }

        @Override
        public synchronized void init() {
            this.server.open();
        }

        @Override
        public synchronized void destroy() {
            this.server.close();
        }

        @Override
        public boolean isAvailable() {
            return this.server.isAvailable();
        }
    }
}
