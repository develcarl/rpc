package org.develcarl.rpc.client;

import org.apache.log4j.Logger;
import org.develcarl.rpc.exception.RpcMethodNotFoundException;
import org.develcarl.rpc.future.RpcFuture;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yichen
 * @description
 * @date 2018/5/16
 **/
public class RpcClientAsyncProxy {

    private static final Logger logger = Logger.getLogger(RpcClientAsyncProxy.class);

    private RpcClient rpcClient;

    private Set<String> serviceMethodSet = new HashSet<>();

    public RpcClientAsyncProxy(RpcClient rpcClient, Class<?> interfaceClass) {
        this.rpcClient = rpcClient;
        Method[] methods = interfaceClass.getMethods();
        for (Method m : methods){
            serviceMethodSet.add(m.getName());
        }
    }

    public RpcFuture call(String methodName, Object... args){
        if(!serviceMethodSet.contains(methodName)){
            throw new RpcMethodNotFoundException(methodName);
        }

        RpcFuture callResult = rpcClient.call(methodName, args);

        if(callResult != null){
            return callResult;

        } else {
            logger.error("RpcClient is unavailable when disconnect with the server.");
            return null;
        }
    }
}
