package org.develcarl.rpc.context;

/**
 * @author yichen
 * @description
 * @date 2018/5/18
 **/
public class RpcRequest {

    private int id;

    private String methodName;

    private Object[] args;

    public RpcRequest(int id, String methodName, Object[] args) {
        this.id = id;
        this.methodName = methodName;
        this.args = args;
    }

    public int getId() {
        return id;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArgs() {
        return args;
    }
}
