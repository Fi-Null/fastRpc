package com.fast.rpc.exception;

/**
 * @ClassName AbstractRpcException
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/24 23:08
 * @Version 1.0
 **/
public class AbstractRpcException extends RuntimeException {

    private static final long serialVersionUID = -5396234117892123976L;

    public AbstractRpcException() {
    }

    public AbstractRpcException(String message) {
        super(message);
    }

    public AbstractRpcException(Throwable cause) {
        super(cause);
    }

    public AbstractRpcException(String message, Throwable cause) {
        super(message, cause);
    }
}