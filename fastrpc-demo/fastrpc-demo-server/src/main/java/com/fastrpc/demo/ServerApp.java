package com.fastrpc.demo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @ClassName ServerApp
 * @Description TODO
 * @Author xiangke
 * @Date 2019/7/3 23:16
 * @Version 1.0
 **/
public class ServerApp {

    public static void main( String[] args ) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:mango-server.xml");
        System.out.println("server start...");
    }
}
