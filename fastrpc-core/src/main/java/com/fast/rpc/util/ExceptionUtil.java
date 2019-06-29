package com.fast.rpc.util;

import com.fast.rpc.exception.RpcBizException;

/**
 * @ClassName ExceptionUtil
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 22:15
 * @Version 1.0
 **/
public class ExceptionUtil {

    /**
     * 判定是否是业务方的逻辑抛出的异常
     *
     * <pre>
     * 		true: 来自业务方的异常
     * 		false: 来自框架本身的异常
     * </pre>
     *
     * @param e
     * @return
     */
    public static boolean isBizException(Exception e) {
        return e instanceof RpcBizException;
    }
}
