package com.fast.rpc.enums;

/**
 * @ClassName FutureState
 * @Description TODO
 * @Author xiangke
 * @Date 2019/7/1 23:50
 * @Version 1.0
 **/
public enum FutureState {
    /**
     * the task is doing
     **/
    NEW(0),
    /**
     * the task is done
     **/
    DONE(1),
    /**
     * ths task is cancelled
     **/
    CANCELLED(2);

    private int state;

    FutureState(int state) {
        this.state = state;
    }
}

