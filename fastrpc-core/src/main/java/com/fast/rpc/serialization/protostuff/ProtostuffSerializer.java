package com.fast.rpc.serialization.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.fast.rpc.codec.Serializer;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName ProtostuffSerializer
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 15:02
 * @Version 1.0
 **/
public class ProtostuffSerializer implements Serializer {

    private static final LoadingCache<Class<?>, Schema<?>> schemas = CacheBuilder.newBuilder()
            .build(new CacheLoader<Class<?>, Schema<?>>() {
                @Override
                public Schema<?> load(Class<?> cls) {

                    return RuntimeSchema.createFrom(cls);
                }
            });

    @Override
    public byte[] serialize(Object msg) throws IOException {
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema schema = getSchema(msg.getClass());
            byte[] arr = ProtostuffIOUtil.toByteArray(msg, schema, buffer);
            return arr;
        } finally {
            buffer.clear();
        }
    }

    @Override
    public <T> T deserialize(byte[] buf, Class<T> type) throws IOException {
        Schema<T> schema = getSchema(type);
        T msg = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(buf, msg, schema);
        return msg;
    }

    private static Schema getSchema(Class<?> cls) throws IOException {
        try {
            return schemas.get(cls);
        } catch (ExecutionException e) {
            throw new IOException("create protostuff schema error", e);
        }
    }
}
