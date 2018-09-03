package org.develcarl.rpc.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.develcarl.rpc.serializer.KryoSerializer;

/**
 * @author yichen
 * @description
 * @date 2018/6/12
 **/
public class NettyKryoEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        KryoSerializer.serialize(msg, out);
    }
}
