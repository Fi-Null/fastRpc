package com.fast.rpc.rpc;

import com.fast.rpc.common.URL;
import com.fast.rpc.core.Request;
import com.fast.rpc.core.Response;
import com.fast.rpc.exception.RpcFrameworkException;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName AbstractReference
 * @Description TODO
 * @Author xiangke
 * @Date 2019/7/1 23:41
 * @Version 1.0
 **/
public abstract class AbstractReference<T> implements Reference<T> {

    protected Class<T> clz;
    private URL url;
    protected URL serviceUrl;

    protected AtomicInteger activeCounter = new AtomicInteger(0);

    public AbstractReference(Class<T> clz, URL serviceUrl) {
        this.clz = clz;
        this.serviceUrl = serviceUrl;
    }

    public AbstractReference(Class<T> clz, URL url, URL serviceUrl) {
        this.clz = clz;
        this.url = url;
        this.serviceUrl = serviceUrl;
    }

    @Override
    public URL getServiceUrl() {
        return serviceUrl;
    }

    @Override
    public Class<T> getInterface() {
        return clz;
    }

    @Override
    public Response call(Request request) {
        if (!isAvailable()) {
            throw new RpcFrameworkException(this.getClass().getName() + " call Error: node is not available, url=" + url.getUri());
        }

        incrActiveCount(request);
        Response response = null;
        try {
            response = doCall(request);
            return response;
        } finally {
            decrActiveCount(request, response);
        }

    }

    @Override
    public int activeCount() {
        return activeCounter.get();
    }

    protected abstract Response doCall(Request request);

    protected void decrActiveCount(Request request, Response response) {
        activeCounter.decrementAndGet();
    }

    protected void incrActiveCount(Request request) {
        activeCounter.incrementAndGet();
    }

    @Override
    public String desc() {
        return "[" + this.getClass().getName() + "] url=" + url;
    }

    @Override
    public URL getUrl() {
        return url;
    }
}
