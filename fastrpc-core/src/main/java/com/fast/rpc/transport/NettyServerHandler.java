package com.fast.rpc.transport;

import com.fast.rpc.common.Constants;
import com.fast.rpc.core.DefaultRequest;
import com.fast.rpc.core.DefaultResponse;
import com.fast.rpc.exception.RpcFrameworkException;
import com.fast.rpc.rpc.MessageRouter;
import com.fast.rpc.rpc.RpcContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName NettyServerHandler
 * @Description TODO
 * @Author xiangke
 * @Date 2019/6/29 01:17
 * @Version 1.0
 **/
public class NettyServerHandler extends SimpleChannelInboundHandler<DefaultRequest> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private ThreadPoolExecutor pool;

    private MessageRouter router;

    public NettyServerHandler(ThreadPoolExecutor pool, MessageRouter router) {
        this.pool = pool;
        this.router = router;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, DefaultRequest request) throws Exception {

        logger.info("Rpc server receive request id:{}", request.getRequestId());
        //处理请求
        processRpcRequest(context, request);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("NettyServerHandler exceptionCaught: remote=" + ctx.channel().remoteAddress()
                + " local=" + ctx.channel().localAddress(), cause);
        ctx.channel().close();
    }


    /**
     * 处理客户端请求
     **/
    private void processRpcRequest(final ChannelHandlerContext context, final DefaultRequest request) {
        final long processStartTime = System.currentTimeMillis();
        try {
            this.pool.execute(() -> {
                try {
                    RpcContext.init(request);
                    processRpcRequest(context, request, processStartTime);
                } finally {
                    RpcContext.destroy();
                }

            });
        } catch (RejectedExecutionException e) {
            DefaultResponse response = new DefaultResponse();
            response.setRequestId(request.getRequestId());
            response.setException(new RpcFrameworkException("process thread pool is full, reject"));
            response.setProcessTime(System.currentTimeMillis() - processStartTime);
            context.channel().write(response);
        }

    }

    private void processRpcRequest(ChannelHandlerContext context, DefaultRequest request, long processStartTime) {

        DefaultResponse response = (DefaultResponse) this.router.handle(request);//;
        response.setProcessTime(System.currentTimeMillis() - processStartTime);
        if (request.getType() != Constants.REQUEST_ONEWAY) {    //非单向调用
            context.writeAndFlush(response);
        }
        logger.info("Rpc server process request:{} end...", request.getRequestId());
    }

}
