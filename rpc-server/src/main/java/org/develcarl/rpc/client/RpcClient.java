package org.develcarl.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;
import org.develcarl.rpc.aop.RpcInvokeHook;
import org.develcarl.rpc.context.RpcRequest;
import org.develcarl.rpc.future.RpcFuture;
import org.develcarl.rpc.netty.NettyKryoDecoder;
import org.develcarl.rpc.netty.NettyKryoEncoder;
import org.develcarl.rpc.test.TestInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yichen
 * @description
 * @date 2018/5/16
 **/
public class RpcClient implements InvocationHandler {

    private static final Logger logger = Logger.getLogger(RpcClient.class);

    private long timeoutMills;

    private RpcInvokeHook rpcInvokeHook;

    private String host;

    private int port;

    private RpcClientResponseHandler rpcClientResponseHandler;

    private AtomicInteger invokeIdGenerator = new AtomicInteger(0);

    private Bootstrap bootstrap;

    private Channel channel;

    private RpcClientChannelInactiveListener rpcClientChannelInactiveListener;

    public RpcClient(long timeoutMills, RpcInvokeHook rpcInvokeHook, String host, int port, int threads) {
        this.timeoutMills = timeoutMills;
        this.rpcInvokeHook = rpcInvokeHook;
        this.host = host;
        this.port = port;

        rpcClientResponseHandler = new RpcClientResponseHandler(threads);
        rpcClientChannelInactiveListener = () -> {
            logger.info("connection with server is closed.");
            logger.info("try to reconnect to the server");
            channel = null;
            do {
                channel = tryConnect();
            }while (channel == null);
        };

    }

    public void connect(){
        bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(new NettyKryoDecoder()
                            , new RpcClientDispatchHandler(rpcClientResponseHandler, rpcClientChannelInactiveListener)
                            , new NettyKryoEncoder());
                }
            });
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

            do {
                channel = tryConnect();
            }while (channel == null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcFuture rpcFuture = call(method.getName(), args);

        if (rpcFuture == null){
            logger.info("RpcClient is unavailable when disconnect to the server");
            return null;
        }

        Object result;
        if (timeoutMills == 0){
            result = rpcFuture.get();
        }else {
            result = rpcFuture.get(timeoutMills);
        }

        if (rpcInvokeHook != null){
            rpcInvokeHook.afterInvoke(method.getName(), args);
        }
        return result;
    }

    public RpcFuture call(String methodName, Object... args){
        if (rpcInvokeHook != null){
            rpcInvokeHook.beforeInvoke(methodName, args);
        }

        RpcFuture rpcFuture = new RpcFuture();
        int id = invokeIdGenerator.addAndGet(1);
        rpcClientResponseHandler.register(id, rpcFuture);

        RpcRequest rpcRequest = new RpcRequest(id, methodName, args);

        if (channel != null){
            channel.writeAndFlush(rpcRequest);
        }else {
            return null;
        }
        return rpcFuture;
    }

    private Channel tryConnect(){

        try {
            logger.info("Try to connect to [" + host + ":" + port + "]");
            ChannelFuture future = bootstrap.connect(host, port).sync();
            if (future.isSuccess()){
                logger.info("Connect to [" + host + ":" + port + "] success.");
                return future.channel();
            }else {
                logger.error("Connect to [" + host + ":" + port + "] failed.");
                logger.info("Retry in 10 seconds");
                Thread.sleep(10000);
                return null;
            }
        }catch (Exception e){
            logger.error("Connect to [" + host + ":" + port + "] failed.");
            logger.info("Retry in 10 seconds");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return null;
        }
    }
}
