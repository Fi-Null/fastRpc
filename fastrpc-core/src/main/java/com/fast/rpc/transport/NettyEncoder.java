package com.fast.rpc.transport;

import com.fast.rpc.codec.Codec;
import com.fast.rpc.common.Constants;
import com.fast.rpc.common.URL;
import com.fast.rpc.core.DefaultResponse;
import com.fast.rpc.core.Request;
import com.fast.rpc.core.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName NettyEncoder
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 01:14
 * @Version 1.0
 **/
public class NettyEncoder extends MessageToByteEncoder {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Codec codec;

    private URL url;

    public NettyEncoder(Codec codec, URL url) {
        this.codec = codec;
        this.url = url;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        long requestId = getRequestId(msg);
        byte[] data = null;

        if (msg instanceof Response) {
            try {
                data = codec.encode(url, msg);
            } catch (Exception e) {
                logger.error("RpcEncoder encode error, requestId=" + requestId, e);
                Response response = buildExceptionResponse(requestId, e);
                data = codec.encode(url, response);
            }
        } else {
            data = codec.encode(url, msg);
        }

        out.writeShort(Constants.NETTY_MAGIC_TYPE);
        out.writeByte(getType(msg));
        out.writeLong(requestId);
        out.writeInt(data.length);

        out.writeBytes(data);
    }

    private byte getType(Object message) {
        if (message instanceof Request) {
            return Constants.FLAG_REQUEST;
        } else if (message instanceof Response) {
            return Constants.FLAG_RESPONSE;
        } else {
            return Constants.FLAG_OTHER;
        }
    }

    private long getRequestId(Object message) {
        if (message instanceof Request) {
            return ((Request) message).getRequestId();
        } else if (message instanceof Response) {
            return ((Response) message).getRequestId();
        } else {
            return 0;
        }
    }

    private Response buildExceptionResponse(long requestId, Exception e) {
        DefaultResponse response = new DefaultResponse();
        response.setRequestId(requestId);
        response.setException(e);
        return response;
    }
}
