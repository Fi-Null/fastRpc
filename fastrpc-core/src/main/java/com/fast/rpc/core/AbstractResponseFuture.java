package com.fast.rpc.core;

import com.fast.rpc.enums.FutureState;

/**
 * @ClassName AbstractResponseFuture
 * @Description TODO
 * @Author xiangke
 * @Date 2019/7/1 23:49
 * @Version 1.0
 **/
public abstract class AbstractResponseFuture<T> implements ResponseFuture<T> {
    protected volatile FutureState state = FutureState.NEW; //状态

    protected final long createTime = System.currentTimeMillis();//处理开始时间

    protected long timeoutInMillis;

    public AbstractResponseFuture(long timeoutInMillis) {
        this.timeoutInMillis = timeoutInMillis;
    }

    @Override
    public boolean isCancelled() {
        return this.state == FutureState.CANCELLED;
    }

    @Override
    public boolean isDone() {
        return this.state == FutureState.DONE;
    }

    @Override
    public boolean isTimeout() {
        return createTime + timeoutInMillis > System.currentTimeMillis();
    }

}
