package com.fast.rpc.exception;

/**
 * @ClassName RpcBizException
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/26 23:41
 * @Version 1.0
 **/
public class RpcBizException extends AbstractRpcException {

    private static final long serialVersionUID = -375026939799883513L;

    public RpcBizException() {

    }

    public RpcBizException(String message) {
        super(message);
    }

    public RpcBizException(Throwable cause) {
        super(cause);
    }

    public RpcBizException(String message, Throwable cause) {
        super(message, cause);
    }
}
