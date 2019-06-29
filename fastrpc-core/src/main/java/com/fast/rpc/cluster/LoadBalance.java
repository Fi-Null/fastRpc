package com.fast.rpc.cluster;

import com.fast.rpc.annotation.SPI;
import com.fast.rpc.core.Request;
import com.fast.rpc.rpc.Reference;

import java.util.List;

/**
 * @ClassName LoadBalance
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 22:08
 * @Version 1.0
 **/
@SPI
public interface LoadBalance<T> {
    void setReferences(List<Reference<T>> references);

    Reference<T> select(Request request);
}
