package com.fast.rpc.codec;

import java.io.IOException;

/**
 * @ClassName AbstractCodec
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 14:12
 * @Version 1.0
 **/
public abstract class AbstractCodec implements Codec {

    protected byte[] serialize(Object message, Serializer serializer) throws IOException {
        if (message == null) {
            return null;
        }
        return serializer.serialize(message);
    }

    protected Object deserialize(byte[] data, Class<?> type, Serializer serializer) throws IOException {
        if (data == null) {
            return null;
        }
        return serializer.deserialize(data, type);
    }
}
