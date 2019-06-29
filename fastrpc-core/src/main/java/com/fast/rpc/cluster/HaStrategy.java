package com.fast.rpc.cluster;

import com.fast.rpc.annotation.SPI;
import com.fast.rpc.core.Request;
import com.fast.rpc.core.Response;

/**
 * @InterfaeName HaStrategy
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 22:12
 * @Version 1.0
 **/

@SPI
public interface HaStrategy<T> {

    Response call(Request request, LoadBalance loadBalance);
}
