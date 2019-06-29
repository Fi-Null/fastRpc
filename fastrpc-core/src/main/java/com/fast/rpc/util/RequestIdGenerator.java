package com.fast.rpc.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @ClassName RequestIdGenerator
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 22:31
 * @Version 1.0
 **/
public class RequestIdGenerator {

    private static final AtomicLong idGenerator = new AtomicLong(1);

    public static long getRequestId() {
        return idGenerator.getAndIncrement();
    }
}
