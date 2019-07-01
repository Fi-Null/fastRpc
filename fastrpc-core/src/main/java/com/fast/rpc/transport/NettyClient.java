package com.fast.rpc.transport;

import com.fast.rpc.core.Request;
import com.fast.rpc.core.Response;
import com.fast.rpc.core.ResponseFuture;
import com.fast.rpc.exception.TransportException;

/**
 * @ClassName NettyClient
 * @Description TODO
 * @Author xiangke
 * @Date 2019/7/1 23:44
 * @Version 1.0
 **/
public interface NettyClient extends Endpoint {

    Response invokeSync(final Request request)
            throws InterruptedException, TransportException;

    ResponseFuture invokeAsync(final Request request)
            throws InterruptedException, TransportException;

    void invokeOneway(final Request request)
            throws InterruptedException, TransportException;

}