package org.develcarl.rpc.exception;

/**
 * @author yichen
 * @description
 * @date 2018/6/13
 **/
public class RpcTimeoutException extends Throwable {

    public RpcTimeoutException(String message) {
        super(message);
    }
}
