package org.develcarl.rpc.server;

import org.develcarl.rpc.aop.RpcInvokeHook;

/**
 * @author yichen
 * @description
 * @date 2018/5/18
 **/
public class RpcServerBuilder {

    private Class<?> interfaceClass;

    private Object serviceProvider;

    private int port;

    private int threads;

    private RpcInvokeHook rpcInvokeHook;

    public static RpcServerBuilder create(){
        return new RpcServerBuilder();
    }

    public RpcServerBuilder serviceInterface(Class<?> interfaceClass){
        this.interfaceClass = interfaceClass;
        return this;
    }

    public RpcServerBuilder serviceProvider(Object serviceProvider){
        this.serviceProvider = serviceProvider;
        return this;
    }

    public RpcServerBuilder bind(int port){
        this.port = port;
        return this;
    }

    public RpcServerBuilder threads(int threadCount){
        this.threads = threadCount;
        return this;
    }

    public RpcServerBuilder hook(RpcInvokeHook rpcInvokeHook){
        this.rpcInvokeHook = rpcInvokeHook;
        return this;
    }

    public RpcServer build(){
        if (threads <= 0){
            threads = Runtime.getRuntime().availableProcessors();
        }

        RpcServer rpcServer = new RpcServer(interfaceClass, serviceProvider, port, threads, rpcInvokeHook);

        return rpcServer;
    }

}
