package org.develcarl.rpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import org.apache.log4j.Logger;

/**
 * @author yichen
 * @description
 * @date 2018/6/12
 **/
public class KryoSerializer {

    private static final Logger logger = Logger.getLogger(KryoSerializer.class);

    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    private static final int BUFFER_SIZE = 1024 * 4;

    private static final int MAX_BUFFER_SIZE = -1;

    public static void serialize(Object object, ByteBuf byteBuf){

        Kryo kryo = KryoHolder.get();
        int startIdx = byteBuf.writerIndex();
        ByteBufOutputStream byteBufOutputStream = new ByteBufOutputStream(byteBuf);
        try {
            byteBufOutputStream.write(LENGTH_PLACEHOLDER);
            Output output = new Output(BUFFER_SIZE, MAX_BUFFER_SIZE);
            output.setOutputStream(byteBufOutputStream);
            kryo.writeClassAndObject(output, object);

            output.flush();
            output.close();

            int endIds = byteBuf.writerIndex();

            byteBuf.setInt(startIdx, endIds - startIdx - 4);

        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    public static Object deserialize(ByteBuf byteBuf){
        if (byteBuf == null){
            return null;
        }

        Input input = new Input(new ByteBufInputStream(byteBuf));
        Kryo kryo = KryoHolder.get();
        return kryo.readClassAndObject(input);
    }

}
