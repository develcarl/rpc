package org.develcarl.rpc.server;

import com.esotericsoftware.reflectasm.MethodAccess;
import io.netty.channel.Channel;
import org.develcarl.rpc.aop.RpcInvokeHook;
import org.develcarl.rpc.context.RpcRequestWrapper;
import org.develcarl.rpc.context.RpcResponse;

import java.util.concurrent.BlockingQueue;

/**
 * @author yichen
 * @description
 * @date 2018/5/18
 **/
public class RpcServerRequestHandleRunnable implements Runnable {

    private Class<?> interfaceClass;

    private Object serviceProvider;

    private RpcInvokeHook rpcInvokeHook;

    private BlockingQueue<RpcRequestWrapper> requestQueue;

    private RpcRequestWrapper rpcRequestWrapper;

    private MethodAccess methodAccess;

    private String lastMethodName;

    private int lastMethodIndex;

    public RpcServerRequestHandleRunnable(Class<?> interfaceClass, Object serviceProvider, RpcInvokeHook rpcInvokeHook, BlockingQueue<RpcRequestWrapper> requestQueue) {
        this.interfaceClass = interfaceClass;
        this.serviceProvider = serviceProvider;
        this.rpcInvokeHook = rpcInvokeHook;
        this.requestQueue = requestQueue;

        methodAccess = MethodAccess.get(interfaceClass);
    }

    @Override
    public void run() {
        while (true){
            try {
                rpcRequestWrapper = requestQueue.take();

                String methodName = rpcRequestWrapper.getRpcRequest().getMethodName();
                Object[] args = rpcRequestWrapper.getRpcRequest().getArgs();

                if (rpcInvokeHook != null){
                    rpcInvokeHook.beforeInvoke(methodName, args);
                }

                Object result = null;
                if (!methodName.equals(lastMethodName)){
                    lastMethodIndex = methodAccess.getIndex(methodName);
                    lastMethodName = methodName;
                }

                result = methodAccess.invoke(serviceProvider, lastMethodIndex, args);

                Channel channel = rpcRequestWrapper.getChannel();

                RpcResponse rpcResponse = new RpcResponse(rpcRequestWrapper.getRpcRequest().getId(), result, true);
                channel.writeAndFlush(rpcResponse);

                if (rpcInvokeHook != null){
                    rpcInvokeHook.afterInvoke(methodName, args);
                }

            }catch (Exception e){
                Channel channel = rpcRequestWrapper.getChannel();
                RpcResponse rpcResponse = new RpcResponse(rpcRequestWrapper.getRpcRequest().getId(), e, false);
                channel.writeAndFlush(rpcResponse);
            }

        }
    }
}
