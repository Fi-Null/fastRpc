package com.fast.rpc.rpc;

import com.fast.rpc.core.Request;
import com.fast.rpc.core.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RpcContext
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 01:22
 * @Version 1.0
 **/
public class RpcContext {
    private Map<Object, Object> attribute = new HashMap<>();
    private Request request;
    private Response response;
    private Long requestId;

    private static final ThreadLocal<RpcContext> localContext = ThreadLocal.withInitial(() -> new RpcContext());

    public static RpcContext getContext() {
        return localContext.get();
    }

    public static RpcContext init(Request request) {
        RpcContext context = new RpcContext();
        if (request != null) {
            context.setRequest(request);
            context.setRequestId(request.getRequestId());
        }
        localContext.set(context);
        return context;
    }

    public static void destroy() {
        localContext.remove();
    }

    public void putAttribute(Object key, Object value) {
        attribute.put(key, value);
    }

    public Object getAttribute(Object key) {
        return attribute.get(key);
    }

    public void removeAttribute(Object key) {
        attribute.remove(key);
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Long getRequestId() {
        return requestId;
    }

    void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
}
