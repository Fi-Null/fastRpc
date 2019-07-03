package com.fast.rpc.rpc;

import com.fast.rpc.common.URL;

/**
 * @ClassName Reference
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/26 23:50
 * @Version 1.0
 **/
public interface Reference<T> extends Caller<T> {

    /**
     * 当前Reference的调用次数
     *
     * @return
     */
    int activeCount();

    URL getServiceUrl();
}
