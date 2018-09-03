package org.develcarl.rpc.context;

/**
 * @author yichen
 * @description
 * @date 2018/6/12
 **/
public class RpcResponse {

    private int id;

    private Object result;

    private Throwable throwable;

    private boolean isInvokeSuccess;

    public RpcResponse(int id, Object resultOrThrowable, boolean isInvokeSuccess) {
        this.id = id;
        this.isInvokeSuccess = isInvokeSuccess;

        if (isInvokeSuccess){
            result = resultOrThrowable;
        }else {
            throwable = (Throwable) resultOrThrowable;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public boolean isInvokeSuccess() {
        return isInvokeSuccess;
    }

    public void setInvokeSuccess(boolean invokeSuccess) {
        isInvokeSuccess = invokeSuccess;
    }
}
