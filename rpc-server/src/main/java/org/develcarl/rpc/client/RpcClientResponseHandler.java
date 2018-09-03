package org.develcarl.rpc.client;

import org.develcarl.rpc.context.RpcResponse;
import org.develcarl.rpc.future.RpcFuture;

import java.util.concurrent.*;

/**
 * @author yichen
 * @description
 * @date 2018/6/13
 **/
public class RpcClientResponseHandler {

    private ConcurrentMap<Integer, RpcFuture> invokeIdRpcFutureMap = new ConcurrentHashMap<>();

    private ExecutorService threadPool;

    private BlockingQueue<RpcResponse> responseQueue = new LinkedBlockingDeque<>();

    public RpcClientResponseHandler(int threads){
        threadPool = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++){
            threadPool.execute(new RpcClientResponseHandleRunnable(invokeIdRpcFutureMap, responseQueue));
        }
    }

    public void register(int id, RpcFuture rpcFuture){
        invokeIdRpcFutureMap.put(id, rpcFuture);
    }

    public void addResponse(RpcResponse rpcResponse){
        responseQueue.add(rpcResponse);
    }

}
