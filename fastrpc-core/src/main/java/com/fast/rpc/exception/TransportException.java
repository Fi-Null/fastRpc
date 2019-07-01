package com.fast.rpc.exception;

/**
 * @ClassName TransportException
 * @Description TODO
 * @Author xiangke
 * @Date 2019/7/1 23:45
 * @Version 1.0
 **/
public class TransportException extends AbstractRpcException {

    private static final long serialVersionUID = 1391824218667687554L;

    public TransportException() {
    }

    public TransportException(String message) {
        super(message);
    }

    public TransportException(String message, Throwable cause) {
        super(message, cause);
    }
}
