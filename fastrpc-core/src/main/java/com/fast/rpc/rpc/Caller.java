package com.fast.rpc.rpc;

/**
 * @InterfaceName Caller
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/23 23:28
 * @Version 1.0
 **/
public interface Caller<T> extends Node {

    Class<T> getInterface();
}
