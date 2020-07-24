package com.ido.netty.handler;

import com.ido.netty.manager.TargetContextHolder;
import com.ido.netty.proto.DataInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import java.net.URI;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf data = (ByteBuf) msg;
        Channel targetChannel = TargetContextHolder.getChannel("/home");
        if (targetChannel == null) {
            return;

        }
        byte[] d = new byte[data.readableBytes()];
        data.getBytes(0,d);
        String body = new String(d);
        DataInfo.testBuf bu = DataInfo.testBuf.newBuilder().setData(body).setID(1).build();
        ChannelFuture cf = targetChannel.writeAndFlush(bu);
        cf.addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("proxy sent success");
            }
        });
        ByteBuf ret = Unpooled.copiedBuffer("sent".getBytes());
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, ret);
        response.headers().add("content-type", "text/plain");
        response.headers().add("content-length", ret.readableBytes());
        ctx.writeAndFlush(response);
        System.out.println("response");
        super.channelRead(ctx, msg);
    }
}
