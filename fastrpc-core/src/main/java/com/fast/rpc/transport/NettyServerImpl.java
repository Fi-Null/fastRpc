package com.fast.rpc.transport;

import com.fast.rpc.common.Constants;
import com.fast.rpc.common.URL;
import com.fast.rpc.common.URLParam;
import com.fast.rpc.enums.ChannelState;
import com.fast.rpc.rpc.MessageRouter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName NettyServerImpl
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 00:35
 * @Version 1.0
 **/
public class NettyServerImpl extends AbstractServer {

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);

    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private ServerBootstrap serverBootstrap = new ServerBootstrap();

    /**
     * 业务处理线程池
     */
    private ThreadPoolExecutor pool;

    private MessageRouter router;

    /**
     * 初始化标识，防止重复初始化
     */
    private volatile boolean initializing = false;


    public NettyServerImpl(URL serviceUrl, MessageRouter router) {
        super(serviceUrl);
        this.localAddress = new InetSocketAddress(serviceUrl.getPort());
        this.router = router;
        this.pool = new ThreadPoolExecutor(serviceUrl.getIntParameter(URLParam.minWorkerThread.getName(), URLParam.minWorkerThread.getIntValue()),
                serviceUrl.getIntParameter(URLParam.maxWorkerThread.getName(), URLParam.maxWorkerThread.getIntValue()),
                120, TimeUnit.SECONDS, new LinkedBlockingQueue(),
                new DefaultThreadFactory(String.format("%s-%s", Constants.FRAMEWORK_NAME, "biz")));
    }

    @Override
    public synchronized boolean open() {
        if (initializing) {
            logger.warn("NettyServer ServerChannel is initializing: serviceUrl=" + serviceUrl);
            return true;
        }

        initializing = true;

        if (state.isAvailable()) {
            logger.warn("NettyServer ServerChannel has initialized: serviceUrl=" + serviceUrl);
            return true;
        }

        // 最大响应包限制
        final int maxContentLength = serviceUrl.getIntParameter(URLParam.maxContentLength.getName(),
                URLParam.maxContentLength.getIntValue());

        this.serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_RCVBUF, serviceUrl.getIntParameter(URLParam.bufferSize.getName(), URLParam.bufferSize.getIntValue()))
                .childOption(ChannelOption.SO_SNDBUF, serviceUrl.getIntParameter(URLParam.bufferSize.getName(), URLParam.bufferSize.getIntValue()))
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new NettyDecoder(codec, serviceUrl, maxContentLength, Constants.HEADER_SIZE, 4),
                                new NettyEncoder(codec, serviceUrl), //
                                new NettyServerHandler(pool, router));
                    }
                });

        return false;
    }

    @Override
    public boolean isAvailable() {
        return state.isAvailable();
    }

    @Override
    public boolean isClosed() {
        return state.isClosed();
    }

    @Override
    public URL getUrl() {
        return serviceUrl;
    }

    @Override
    public void close() {
        close(0);
    }

    @Override
    public void close(int timeout) {
        if (state.isClosed()) {
            logger.info("NettyServer close fail: already close, serviceUrl={}", serviceUrl.getUri());
            return;
        }

        try {
            this.bossGroup.shutdownGracefully();
            this.workerGroup.shutdownGracefully();
            this.pool.shutdown();

            state = ChannelState.CLOSED;
        } catch (Exception e) {
            logger.error("NettyServer close Error: serviceUrl=" + serviceUrl.getUri(), e);
        }
    }
}
