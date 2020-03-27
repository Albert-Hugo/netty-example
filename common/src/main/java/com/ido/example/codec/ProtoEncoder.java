package com.ido.example.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static com.ido.example.codec.ProtoDecoder.ID_SITE;
import static com.ido.example.codec.ProtoDecoder.TYPE_SIZE;

public class ProtoEncoder extends MessageToByteEncoder<ProtoMsg> {


    @Override
    protected void encode(ChannelHandlerContext ctx, ProtoMsg msg, ByteBuf out) throws Exception {

        int bodyLength;
        if (msg.data != null) {
            bodyLength = msg.data.length + TYPE_SIZE + ID_SITE;
        } else {
            bodyLength = TYPE_SIZE + ID_SITE;
        }
        // write the total packet length but without length field's length.
        out.writeInt(bodyLength);

        out.writeByte(msg.type);
        out.writeLong(msg.id);
        if (msg.data != null) {
            out.writeBytes(msg.data);
        }
    }
}