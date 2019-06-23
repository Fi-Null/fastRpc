package com.fast.rpc.rpc;

/**
 * @InterfaceName Exporter
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/23 23:27
 * @Version 1.0
 **/
public interface Exporter<T> extends Node {
    Provider<T> getProvider();

    void unexport();
}
