package com.fast.rpc.codec;

import com.fast.rpc.common.Constants;
import com.fast.rpc.common.URL;
import com.fast.rpc.common.URLParam;
import com.fast.rpc.core.DefaultRequest;
import com.fast.rpc.core.DefaultResponse;
import com.fast.rpc.core.ExtensionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @ClassName DefaultCodec
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 14:16
 * @Version 1.0
 **/
public class DefaultCodec extends AbstractCodec {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public byte[] encode(URL url, Object message) throws IOException {
        String serialization = url.getParameter(URLParam.serialization.getName(), URLParam.serialization.getValue());
        logger.info("Codec encode serialization:{}", serialization);
        return serialize(message, ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(serialization));

    }

    @Override
    public Object decode(URL url, byte messageType, byte[] data) throws IOException {
        String serialization = url.getParameter(URLParam.serialization.getName(), URLParam.serialization.getValue());
        logger.info("Codec decode serialization:{}", serialization);
        if (messageType == Constants.FLAG_REQUEST) {
            return deserialize(data, DefaultRequest.class, ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(serialization));
        }
        return deserialize(data, DefaultResponse.class, ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(serialization));
    }
}
