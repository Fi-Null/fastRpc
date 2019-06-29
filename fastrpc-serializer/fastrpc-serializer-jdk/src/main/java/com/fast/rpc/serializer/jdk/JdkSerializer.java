package com.fast.rpc.serializer.jdk;

import com.fast.rpc.codec.Serializer;
import com.fast.rpc.util.IoUtils;

import java.io.*;

/**
 * @ClassName JdkSerializer
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 20:21
 * @Version 1.0
 **/
public class JdkSerializer implements Serializer {
    @Override
    public byte[] serialize(Object msg) throws IOException {
        ObjectOutputStream output = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            output = new ObjectOutputStream(baos);
            output.writeObject(msg);
            output.flush();

            return baos.toByteArray();
        } finally {
            IoUtils.closeQuietly(baos);
            output.close();
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> type) throws IOException {
        // Read Obj from File
        ObjectInputStream input = null;
        try {
            input = new ObjectInputStream(new ByteArrayInputStream(data));
            return (T) input.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("class not found", e);
        } finally {
            IoUtils.closeQuietly(input);
        }
    }
}
