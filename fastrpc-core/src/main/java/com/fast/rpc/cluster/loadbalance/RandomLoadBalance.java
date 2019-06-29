package com.fast.rpc.cluster.loadbalance;

import com.fast.rpc.cluster.LoadBalance;
import com.fast.rpc.core.Request;
import com.fast.rpc.rpc.Reference;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName RandomLoadBalance
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 22:08
 * @Version 1.0
 **/
public class RandomLoadBalance<T> implements LoadBalance<T> {

    private volatile List<Reference<T>> references;

    @Override
    public void setReferences(List<Reference<T>> references) {
        this.references = references;
    }

    @Override
    public Reference<T> select(Request request) {
        int idx = (int) (ThreadLocalRandom.current().nextDouble() * references.size());
        return references.get(idx);
    }
}
