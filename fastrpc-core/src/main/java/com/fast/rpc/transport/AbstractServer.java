package com.fast.rpc.transport;

import com.fast.rpc.codec.Codec;
import com.fast.rpc.common.URL;
import com.fast.rpc.common.URLParam;
import com.fast.rpc.core.ExtensionLoader;
import com.fast.rpc.enums.ChannelState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @ClassName AbstractServer
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 00:13
 * @Version 1.0
 **/
public abstract class AbstractServer implements NettyServer {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected InetSocketAddress localAddress;

    protected InetSocketAddress remoteAddress;

    protected URL url;
    protected Codec codec;

    protected volatile ChannelState state = ChannelState.NEW;

    public AbstractServer() {
    }

    public AbstractServer(URL url) {
        this.url = url;
        this.codec = ExtensionLoader.getExtensionLoader(Codec.class).getExtension(url.getParameter(URLParam.codec.getName(), URLParam.codec.getValue()));
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return localAddress;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }
}
