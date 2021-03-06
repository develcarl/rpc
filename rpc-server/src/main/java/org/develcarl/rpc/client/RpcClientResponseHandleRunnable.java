package org.develcarl.rpc.client;

import org.apache.log4j.Logger;
import org.develcarl.rpc.context.RpcResponse;
import org.develcarl.rpc.future.RpcFuture;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

/**
 * @author yichen
 * @description
 * @date 2018/6/13
 **/
public class RpcClientResponseHandleRunnable implements Runnable {

    private static final Logger logger = Logger.getLogger(RpcClientResponseHandleRunnable.class);

    private ConcurrentMap<Integer, RpcFuture> invokeIdRpcFutureMap;

    private BlockingQueue<RpcResponse> responseQueue;

    public RpcClientResponseHandleRunnable(ConcurrentMap<Integer, RpcFuture> invokeIdRpcFutureMap, BlockingQueue<RpcResponse> responseQueue){
        this.invokeIdRpcFutureMap = invokeIdRpcFutureMap;
        this.responseQueue = responseQueue;
    }

    @Override
    public void run() {
        while (true){
            try {
                RpcResponse rpcResponse = responseQueue.take();

                int id = rpcResponse.getId();
                RpcFuture rpcFuture = invokeIdRpcFutureMap.remove(id);

                if (rpcResponse.isInvokeSuccess()){
                    rpcFuture.setResult(rpcResponse.getResult());
                }else {
                    rpcFuture.setThrowable(rpcResponse.getThrowable());
                }

            }catch (InterruptedException e){
                logger.error(e);
            }
        }
    }
}
