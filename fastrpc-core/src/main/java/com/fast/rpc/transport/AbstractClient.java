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
 * @ClassName AbstractClient
 * @Description TODO
 * @Author xiangke
 * @Date 2019/7/1 23:47
 * @Version 1.0
 **/
public abstract class AbstractClient implements NettyClient {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected InetSocketAddress localAddress;
    protected InetSocketAddress remoteAddress;

    protected URL url;
    protected Codec codec;

    protected volatile ChannelState state = ChannelState.NEW;

    public AbstractClient(URL url) {
        this.url = url;
        this.codec = ExtensionLoader.getExtensionLoader(Codec.class).getExtension(url.getParameter(URLParam.codec.getName(), URLParam.codec.getValue()));
        logger.info("NettyClient init url:" + url.getHost() + "-" + url.getInterfaceName() + ", use codec:" + codec.getClass().getSimpleName());
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
