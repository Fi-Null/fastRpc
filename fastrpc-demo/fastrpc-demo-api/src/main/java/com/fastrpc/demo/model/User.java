package com.fastrpc.demo.model;

import java.io.Serializable;

/**
 * @ClassName User
 * @Description TODO
 * @Author xiangke
 * @Date 2019/7/3 23:06
 * @Version 1.0
 **/
public class User implements Serializable {

    private static final long serialVersionUID = 8989971275886860227L;

    private Long id;
    private String name;
    private String password;
    private int age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                '}';
    }
}