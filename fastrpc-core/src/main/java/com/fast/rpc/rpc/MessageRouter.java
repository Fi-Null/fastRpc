package com.fast.rpc.rpc;

import com.fast.rpc.core.Request;
import com.fast.rpc.core.Response;
import com.fast.rpc.exception.RpcFrameworkException;
import com.fast.rpc.util.FrameworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName MessageRouter
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 00:18
 * @Version 1.0
 **/
public class MessageRouter implements MessageHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ConcurrentHashMap<String, Provider<?>> providers = new ConcurrentHashMap<>();

    public MessageRouter() {
    }

    public MessageRouter(Provider<?> provider) {
        addProvider(provider);
    }

    @Override
    public Response handle(Request request) {
        return null;
    }

    public synchronized void addProvider(Provider<?> provider) {
        String serviceKey = FrameworkUtils.getServiceKey(provider.getUrl());
        if (providers.containsKey(serviceKey)) {
            throw new RpcFrameworkException("provider alread exist: " + serviceKey);
        }
        providers.put(serviceKey, provider);
        logger.info("RequestRouter addProvider: url=" + provider.getUrl());
    }

    public synchronized void removeProvider(Provider<?> provider) {
        String serviceKey = FrameworkUtils.getServiceKey(provider.getUrl());
        providers.remove(serviceKey);
        logger.info("RequestRouter removeProvider: url=" + provider.getUrl());
    }

}
