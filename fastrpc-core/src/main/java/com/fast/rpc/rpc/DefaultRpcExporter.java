package com.fast.rpc.rpc;

import com.fast.rpc.common.URL;
import com.fast.rpc.transport.NettyServer;
import com.fast.rpc.transport.NettyServerImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName DefaultRpcExporter
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 00:06
 * @Version 1.0
 **/
public class DefaultRpcExporter<T> extends AbstractExporter<T> {

    private final ConcurrentHashMap<String, NettyServer> ipPort2Server;

    private final Map<String, MessageRouter> ipPort2RequestRouter;

    private NettyServer server;

    DefaultRpcExporter(Provider<T> provider, URL serviceUrl, ConcurrentHashMap<String, NettyServer> ipPort2Server,
                       Map<String, MessageRouter> ipPort2RequestRouter) {
        super(provider, serviceUrl);
        this.ipPort2Server = ipPort2Server;
        this.ipPort2RequestRouter = ipPort2RequestRouter;
        this.server = initServer(serviceUrl);
    }

    private NettyServer initServer(URL serviceUrl) {
        String ipPort = serviceUrl.getServerAndPort();

        MessageRouter router = initRequestRouter(serviceUrl);

        NettyServer server;
        synchronized (ipPort2Server) {
            server = ipPort2Server.get(ipPort);
            if (server == null) {
                server = new NettyServerImpl(serviceUrl, router);
                ipPort2Server.put(ipPort, server);
            }
        }


        return null;
    }

    private MessageRouter initRequestRouter(URL url) {
        MessageRouter requestRouter;
        String ipPort = url.getServerAndPort();

//        synchronized (ipPort2RequestRouter) {
//            requestRouter = ipPort2RequestRouter.get(ipPort);
//
//            if (requestRouter == null) {
//                requestRouter = new MessageRouter(provider);
//                ipPort2RequestRouter.put(ipPort, requestRouter);
//            } else {
//                requestRouter.addProvider(provider);
//            }
//        }

        //return requestRouter;
        return null;
    }

    @Override
    public void unexport() {

    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean isAvailable() {
        return false;
    }
}
