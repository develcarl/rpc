package org.develcarl.rpc.client;

import org.develcarl.rpc.aop.RpcInvokeHook;

import java.lang.reflect.Proxy;

/**
 * @author yichen
 * @description
 * @date 2018/5/16
 **/
public class RpcClientProxyBuilder {

    public static class ProxyBuilder<T>{

        private Class<T> clazz;
        private RpcClient rpcClient;

        private long timeoutMills = 0;
        private RpcInvokeHook rpcInvokeHook = null;
        private String host;
        private int port;
        private int threads;

        public ProxyBuilder(Class<T> clazz) {
            this.clazz = clazz;
        }

        public ProxyBuilder<T> timeout(int timeoutMills){
            if (timeoutMills < 0){
                throw new IllegalArgumentException("timeoutMills can not be minus");
            }
            this.timeoutMills = timeoutMills;
            return this;
        }

        public ProxyBuilder<T> hook(RpcInvokeHook hook){
            this.rpcInvokeHook = hook;
            return this;
        }

        public ProxyBuilder<T> connect(String host, int port){
            this.host = host;
            this.port = port;
            return this;
        }

        public ProxyBuilder<T> threads(int threads){
            this.threads = threads;
            return this;
        }

        public T build(){
            rpcClient = new RpcClient(timeoutMills, rpcInvokeHook, host, port, threads);
            rpcClient.connect();

            return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, rpcClient);
        }

        public RpcClientAsyncProxy buildAsyncProxy(){
            rpcClient = new RpcClient(timeoutMills, rpcInvokeHook, host, port, threads);
            rpcClient.connect();

            return new RpcClientAsyncProxy(rpcClient, clazz);
        }
    }

    public static <T> ProxyBuilder<T> create(Class<T> targetClass){
        return new ProxyBuilder<T>(targetClass);
    }

}
