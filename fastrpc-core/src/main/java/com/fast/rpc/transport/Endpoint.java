package com.fast.rpc.transport;

import com.fast.rpc.common.URL;

import java.net.InetSocketAddress;

/**
 * @InterfaceName Endpoint
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 00:12
 * @Version 1.0
 **/
public interface Endpoint {

    /**
     * get local socket address.
     *
     * @return local address.
     */
    InetSocketAddress getLocalAddress();

    /**
     * get remote socket address
     *
     * @return
     */
    InetSocketAddress getRemoteAddress();

    boolean open();

    boolean isAvailable();

    boolean isClosed();

    URL getUrl();

    void close();

    /**
     * close the channel gracefully.
     */
    void close(int timeout);
}
