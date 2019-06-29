package com.fast.rpc.exception;

/**
 * @ClassName RpcServiceException
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 22:33
 * @Version 1.0
 **/
public class RpcServiceException extends AbstractRpcException {

    private static final long serialVersionUID = -6585936752307757973L;

    public RpcServiceException() {
    }

    public RpcServiceException(Throwable cause) {
        super(cause);
    }

    public RpcServiceException(String message) {
        super(message);
    }

    public RpcServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
