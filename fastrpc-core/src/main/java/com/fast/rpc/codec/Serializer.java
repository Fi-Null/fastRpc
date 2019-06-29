package com.fast.rpc.codec;

import com.fast.rpc.annotation.SPI;
import com.fast.rpc.enums.Scope;

import java.io.IOException;

/**
 * @ClassName Serializer
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 14:12
 * @Version 1.0
 **/
@SPI(value = "protostuff", scope = Scope.SINGLETON)
public interface Serializer {

    byte[] serialize(Object msg) throws IOException;

    <T> T deserialize(byte[] data, Class<T> type) throws IOException;
}
