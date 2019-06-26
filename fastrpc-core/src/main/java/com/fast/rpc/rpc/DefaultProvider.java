package com.fast.rpc.rpc;

import com.fast.rpc.common.URL;
import com.fast.rpc.core.DefaultResponse;
import com.fast.rpc.core.Request;
import com.fast.rpc.core.Response;
import com.fast.rpc.exception.RpcBizException;
import com.fast.rpc.exception.RpcFrameworkException;

import java.lang.reflect.Method;

/**
 * @ClassName DefaultProvider
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/26 23:32
 * @Version 1.0
 **/
public class DefaultProvider<T> extends AbstractProvider<T> {

    protected T proxyImpl;

    public DefaultProvider(T proxyImpl, URL url, Class<T> clz) {
        super(url, clz);
        this.proxyImpl = proxyImpl;
    }

    @Override
    public Class<T> getInterface() {
        return clz;
    }

    @Override
    public Response invoke(Request request) {

        DefaultResponse response = new DefaultResponse();
        response.setRequestId(request.getRequestId());

        Method method = lookup(request);
        if (method == null) {
            RpcFrameworkException exception =
                    new RpcFrameworkException("Service method not exist: " + request.getInterfaceName() + "." + request.getMethodName());

            response.setException(exception);
            return response;
        }
        try {
            method.setAccessible(true);
            Object result = method.invoke(proxyImpl, request.getArguments());
            response.setResult(result);
        } catch (Exception e) {
            response.setException(new RpcBizException("invoke failure", e));
        }
        return response;
    }


}
