package org.develcarl.rpc.serializer;

import com.esotericsoftware.kryo.Kryo;

/**
 * @author yichen
 * @description
 * @date 2018/6/12
 **/
public class KryoHolder {

    private static ThreadLocal<Kryo> threadLocal = new ThreadLocal<Kryo>(){
        @Override
        protected Kryo initialValue(){
            Kryo kryo = new KryoReflectionFactory();
            return kryo;
        }
    };

    public static Kryo get(){
        return threadLocal.get();
    }

}
