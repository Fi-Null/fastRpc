package com.fast.rpc.rpc;

import com.fast.rpc.core.Request;
import com.fast.rpc.core.Response;

/**
 * @ClassName MessageHandler
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 00:21
 * @Version 1.0
 **/
public interface MessageHandler {

    Response handle(Request request);

}
