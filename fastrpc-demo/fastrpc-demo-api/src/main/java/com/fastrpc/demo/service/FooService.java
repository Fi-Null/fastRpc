package com.fastrpc.demo.service;

/**
 * @ClassName FooService
 * @Description TODO
 * @Author xiangke
 * @Date 2019/7/3 23:08
 * @Version 1.0
 **/
public interface FooService {
    String hello(String name);

    //exception
    String order(String food) throws NullPointerException;

    void pay(String order) throws Exception;
}
