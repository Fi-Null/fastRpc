package com.fast.rpc.exception;

/**
 * @ClassName RpcFrameworkException
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/24 23:09
 * @Version 1.0
 **/
public class RpcFrameworkException extends AbstractRpcException {

    private static final long serialVersionUID = -3361435023080270457L;

    public RpcFrameworkException() {
    }

    public RpcFrameworkException(String message) {
        super(message);
    }

    public RpcFrameworkException(Throwable cause) {
        super(cause);
    }

    public RpcFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
