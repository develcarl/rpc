package org.develcarl.rpc.aop;

/**
 * @author yichen
 * @description
 * @date 2018/5/16
 **/
public interface RpcInvokeHook {

    void beforeInvoke(String methodName, Object... args);

    void afterInvoke(String methodName, Object... args);

}
