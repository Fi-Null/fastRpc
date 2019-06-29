package com.fast.rpc.proxy.jdk;

import com.fast.rpc.proxy.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @ClassName JdkProxyFactory
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 22:28
 * @Version 1.0
 **/
public class JdkProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Class<T> clz, InvocationHandler invocationHandler) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clz}, invocationHandler);
    }
}
