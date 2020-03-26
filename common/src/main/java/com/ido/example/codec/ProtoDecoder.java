package com.ido.example.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author Carl
 * @date 2020/3/25
 */
public class ProtoDecoder extends LengthFieldBasedFrameDecoder {

    static final byte HEADER_SIZE = 4;

    static final int TYPE_SIZE = 1;


    @Override
    protected ProtoMsg decode(ChannelHandlerContext ctx, ByteBuf in2) throws Exception {

        ByteBuf in = (ByteBuf) super.decode(ctx, in2);
        if (in == null) {
            return null;
        }

        int frameLength = in.readInt();//read how big is the data frame length
        ProtoMsg proxyMessage = new ProtoMsg();
        byte type = in.readByte();

        proxyMessage.type = (type);

        byte[] data = new byte[frameLength - TYPE_SIZE];
        in.readBytes(data);
        proxyMessage.data = (data);

        in.release();

        return proxyMessage;


    }

    public ProtoDecoder() {
        super(1024 * 1024, 0, HEADER_SIZE);
    }
}
