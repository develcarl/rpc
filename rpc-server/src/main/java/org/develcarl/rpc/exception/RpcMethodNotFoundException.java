package org.develcarl.rpc.exception;

/**
 * @author yichen
 * @description
 * @date 2018/6/18
 **/
public class RpcMethodNotFoundException extends RuntimeException {

    public RpcMethodNotFoundException(String methodName) {

        super("method " + methodName + " is not found in current service interface!");

    }
}
