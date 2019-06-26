package com.fast.rpc.rpc;

import com.fast.rpc.annotation.SPI;
import com.fast.rpc.core.Request;
import com.fast.rpc.core.Response;

@SPI
public interface Filter {

    Response filter(Caller<?> caller, Request request);

}
