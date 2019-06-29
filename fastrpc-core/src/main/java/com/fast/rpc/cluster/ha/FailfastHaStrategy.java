package com.fast.rpc.cluster.ha;

import com.fast.rpc.cluster.HaStrategy;
import com.fast.rpc.cluster.LoadBalance;
import com.fast.rpc.core.Request;
import com.fast.rpc.core.Response;
import com.fast.rpc.rpc.Reference;

/**
 * @ClassName FailfastHaStrategy
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 22:18
 * @Version 1.0
 **/
public class FailfastHaStrategy<T> implements HaStrategy<T> {

    @Override
    public Response call(Request request, LoadBalance loadBalance) {
        Reference<T> reference = loadBalance.select(request);
        return reference.call(request);
    }
}
