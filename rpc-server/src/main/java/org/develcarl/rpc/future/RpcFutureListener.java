package org.develcarl.rpc.future;

/**
 * @author yichen
 * @description
 * @date 2018/5/16
 **/
public interface RpcFutureListener {

    void onResult(Object result);

    void onException(Throwable throwable);

}
