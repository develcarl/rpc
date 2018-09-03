package org.develcarl.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;
import org.develcarl.rpc.aop.RpcInvokeHook;
import org.develcarl.rpc.netty.NettyKryoDecoder;
import org.develcarl.rpc.netty.NettyKryoEncoder;


/**
 * @author yichen
 * @description
 * @date 2018/5/18
 **/
public class RpcServer {

    private Logger logger = Logger.getLogger(RpcServer.class);

    private Class<?> interfaceClass;
    private Object serviceProvider;

    private int port;
    private int threads;
    private RpcInvokeHook rpcInvokeHook;

    private RpcServerRequestHandler rpcServerRequestHandler;

    protected RpcServer(Class<?> interfaceClass, Object serviceProvider, int port, int threads, RpcInvokeHook rpcInvokeHook) {
        this.interfaceClass = interfaceClass;
        this.serviceProvider = serviceProvider;
        this.port = port;
        this.threads = threads;
        this.rpcInvokeHook = rpcInvokeHook;

        rpcServerRequestHandler = new RpcServerRequestHandler(interfaceClass, serviceProvider, rpcInvokeHook, threads);
        rpcServerRequestHandler.start();
    }

    public void start(){
        EventLoopGroup mainGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(mainGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyKryoDecoder(),
                                    new RpcServerDispatchHandler(rpcServerRequestHandler),
                                    new NettyKryoEncoder());
                        }
                    });
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = bootstrap.bind(port);
            channelFuture.sync();
            Channel channel = channelFuture.channel();
            logger.info("RpcServer started");
            logger.info(interfaceClass.getSimpleName() + "in service.");
            channel.closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mainGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void stop(){
        System.out.println("server stop success");

    }
}
