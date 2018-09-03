package org.develcarl.rpc.server;

import org.develcarl.rpc.aop.RpcInvokeHook;
import org.develcarl.rpc.context.RpcRequest;
import org.develcarl.rpc.context.RpcRequestWrapper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author yichen
 * @description
 * @date 2018/5/18
 **/
public class RpcServerRequestHandler {

    private Class<?> interfaceClass;
    private Object serviceProvider;
    private RpcInvokeHook rpcInvokeHook;

    private int threads;
    private ExecutorService threadPool;
    private BlockingQueue<RpcRequestWrapper> requestQueue = new LinkedBlockingDeque<RpcRequestWrapper>();

    public RpcServerRequestHandler(Class<?> interfaceClass, Object serviceProvider, RpcInvokeHook rpcInvokeHook, int threads) {
        this.interfaceClass = interfaceClass;
        this.serviceProvider = serviceProvider;
        this.rpcInvokeHook = rpcInvokeHook;
        this.threads = threads;
    }

    public void start(){
        threadPool = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++){
            threadPool.execute(new RpcServerRequestHandleRunnable(interfaceClass, serviceProvider, rpcInvokeHook, requestQueue));
        }
    }

    public void addRequest(RpcRequestWrapper rpcRequestWrapper){
        try {
            requestQueue.put(rpcRequestWrapper);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
