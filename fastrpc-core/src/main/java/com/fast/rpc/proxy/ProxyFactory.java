package com.fast.rpc.proxy;

import com.fast.rpc.annotation.SPI;

import java.lang.reflect.InvocationHandler;

/**
 * @ClassName ProxyFactory
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 22:26
 * @Version 1.0
 **/
@SPI(value = "jdk")
public interface ProxyFactory {

    <T> T getProxy(Class<T> clz, InvocationHandler invocationHandler);
}
