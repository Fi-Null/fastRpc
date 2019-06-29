package com.fast.rpc.serialization;

import com.fast.rpc.codec.Serializer;

import java.io.IOException;

/**
 * @ClassName DefaultSerializer
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 14:55
 * @Version 1.0
 **/
public class DefaultSerializer implements Serializer {

    @Override
    public byte[] serialize(Object msg) throws IOException {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> type) throws IOException {
        return null;
    }
}
