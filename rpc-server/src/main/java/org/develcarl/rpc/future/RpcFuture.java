package org.develcarl.rpc.future;

import org.develcarl.rpc.exception.RpcTimeoutException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author yichen
 * @description
 * @date 2018/5/16
 **/
public class RpcFuture {

    public final static int STATE_AWAIT = 0;

    public final static int STATE_SUCCESS = 1;

    public final static int STATE_EXCEPTION = 2;

    private CountDownLatch countDownLatch;

    private Object result;

    private Throwable throwable;

    private volatile int state;

    private RpcFutureListener rpcFutureListener = null;

    public RpcFuture() {
        countDownLatch = new CountDownLatch(1);
        state = STATE_AWAIT;
    }

    public Object get() throws Throwable {
        try {
            countDownLatch.await();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        if (state == STATE_SUCCESS){
            return result;
        }else if (state == STATE_EXCEPTION){
            throw throwable;
        }else {
            throw new RuntimeException("RpcFuture Exception!");
        }
    }

    public Object get(long timeout) throws Throwable{

        boolean awaitSuccess;
        awaitSuccess = countDownLatch.await(timeout, TimeUnit.MILLISECONDS);

        if (!awaitSuccess){
            throw new RpcTimeoutException("RpcFuture Exception!");
        }

        if (state == STATE_SUCCESS){
            return result;
        }else if (state == STATE_EXCEPTION){
            throw throwable;
        }else {
            throw new RuntimeException("RpcFuture Exception!");
        }
    }

    public synchronized void setResult(Object result){
        this.result = result;
        state = STATE_SUCCESS;

        if (rpcFutureListener != null){
            rpcFutureListener.onResult(result);
        }

        countDownLatch.countDown();
    }

    public synchronized void setThrowable(Throwable throwable){
        this.throwable = throwable;
        state = STATE_EXCEPTION;

        if (rpcFutureListener != null){
            rpcFutureListener.onException(throwable);
        }
        countDownLatch.countDown();
    }

    public boolean isDone(){
        return state != STATE_AWAIT;
    }

    public synchronized void setRpcFutureListener(RpcFutureListener rpcFutureListener){
        this.rpcFutureListener = rpcFutureListener;
    }
}
