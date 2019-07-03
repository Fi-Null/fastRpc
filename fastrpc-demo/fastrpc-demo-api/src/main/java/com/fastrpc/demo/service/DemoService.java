package com.fastrpc.demo.service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName DemoService
 * @Description TODO
 * @Author xiangke
 * @Date 2019/7/3 23:07
 * @Version 1.0
 **/
public interface DemoService {
    void hello(String msg);

    String echo(String msg);

    Map<String, String> introduce(String name, List<String> hobbies);
}
