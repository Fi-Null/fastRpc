package com.fast.rpc.rpc;

import com.fast.rpc.core.Request;
import com.fast.rpc.core.Response;

/**
 * @InterfaceName Caller
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/23 23:28
 * @Version 1.0
 **/
public interface Caller<T> extends Node {

    Class<T> getInterface();

    Response call(Request request);

}
