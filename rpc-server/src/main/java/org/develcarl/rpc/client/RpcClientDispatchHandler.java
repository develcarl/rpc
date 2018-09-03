package org.develcarl.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.develcarl.rpc.context.RpcResponse;

/**
 * @author yichen
 * @description
 * @date 2018/6/13
 **/
public class RpcClientDispatchHandler extends ChannelInboundHandlerAdapter {

    private RpcClientResponseHandler rpcClientResponseHandler;
    private RpcClientChannelInactiveListener rpcClientChannelInactiveListener = null;

    public RpcClientDispatchHandler(RpcClientResponseHandler rpcClientResponseHandler, RpcClientChannelInactiveListener rpcClientChannelInactiveListener) {
        this.rpcClientResponseHandler = rpcClientResponseHandler;
        this.rpcClientChannelInactiveListener = rpcClientChannelInactiveListener;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse rpcResponse = (RpcResponse) msg;
        rpcClientResponseHandler.addResponse(rpcResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (rpcClientChannelInactiveListener != null){
            rpcClientChannelInactiveListener.onInactive();
        }
    }
}
