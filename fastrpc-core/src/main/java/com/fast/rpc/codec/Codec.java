package com.fast.rpc.codec;

import com.fast.rpc.annotation.SPI;
import com.fast.rpc.common.Constants;
import com.fast.rpc.common.URL;

import java.io.IOException;

/**
 * @InterfaceName Codec
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 00:14
 * @Version 1.0
 **/
@SPI(Constants.FRAMEWORK_NAME)
public interface Codec {
    
    byte[] encode(URL url, Object message) throws IOException;

    Object decode(URL url, byte messageType, byte[] data) throws IOException;
}
