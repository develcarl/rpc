package org.develcarl.rpc.context;

import io.netty.channel.Channel;

/**
 * @author yichen
 * @description
 * @date 2018/6/13
 **/
public class RpcRequestWrapper {

    private RpcRequest rpcRequest;

    private Channel channel;

    public RpcRequestWrapper(RpcRequest rpcRequest, Channel channel) {
        this.rpcRequest = rpcRequest;
        this.channel = channel;
    }

    public RpcRequest getRpcRequest() {
        return rpcRequest;
    }

    public void setRpcRequest(RpcRequest rpcRequest) {
        this.rpcRequest = rpcRequest;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
