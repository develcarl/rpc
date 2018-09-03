package org.develcarl.rpc.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.develcarl.rpc.context.RpcRequest;
import org.develcarl.rpc.context.RpcRequestWrapper;

/**
 * @author yichen
 * @description
 * @date 2018/6/18
 **/
public class RpcServerDispatchHandler extends ChannelInboundHandlerAdapter {

    private RpcServerRequestHandler rpcServerRequestHandler;

    public RpcServerDispatchHandler(RpcServerRequestHandler rpcServerRequestHandler) {
        this.rpcServerRequestHandler = rpcServerRequestHandler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest rpcRequest = (RpcRequest) msg;
        RpcRequestWrapper rpcRequestWrapper = new RpcRequestWrapper(rpcRequest, ctx.channel());

        rpcServerRequestHandler.addRequest(rpcRequestWrapper);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
    }
}
