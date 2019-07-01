package com.fast.rpc.core;

/**
 * @ClassName ResponseFuture
 * @Description TODO
 * @Author xiangke
 * @Date 2019/7/1 23:44
 * @Version 1.0
 **/
public interface ResponseFuture<T> {

    T get() throws InterruptedException;

    boolean isCancelled();

    boolean isDone();

    boolean isSuccess();

    void setResult(T result);

    void setFailure(Throwable err);

    boolean isTimeout();

}
