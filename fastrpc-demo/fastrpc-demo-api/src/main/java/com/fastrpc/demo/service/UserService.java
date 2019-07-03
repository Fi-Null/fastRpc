package com.fastrpc.demo.service;

import com.fastrpc.demo.model.User;

import java.util.List;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author xiangke
 * @Date 2019/7/3 23:08
 * @Version 1.0
 **/
public interface UserService {

    Long insert(User user);

    List<User> getUsers(int age);

    int update(User user);
}
