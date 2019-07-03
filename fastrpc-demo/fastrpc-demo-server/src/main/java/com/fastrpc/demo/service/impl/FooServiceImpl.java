package com.fastrpc.demo.service.impl;

import com.fastrpc.demo.service.FooService;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class FooServiceImpl implements FooService {
    @Override
    public String hello(String name) {
        return "hello, "+name;
    }

    @Override
    public String order(String food) throws NullPointerException {
        System.out.println("order food:"+food);
        if(food==null) {
            throw new NullPointerException("food can not be empty.");
        }
        return "food:"+food;
    }

    @Override
    public void pay(String order) throws Exception {

    }

}
