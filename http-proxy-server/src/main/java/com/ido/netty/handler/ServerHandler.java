package com.ido.netty.handler;

import com.ido.netty.manager.ResultHolder;
import com.ido.netty.manager.TargetContextHolder;
import com.ido.netty.proto.DataInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf data = (ByteBuf) msg;
        //获取目标客户的channel
        Channel targetChannel = TargetContextHolder.getChannel("/home");
        if (targetChannel == null) {
            return;

        }
        byte[] d = new byte[data.readableBytes()];
        data.getBytes(0, d);
        String body = new String(d);
        DataInfo.testBuf bu = DataInfo.testBuf.newBuilder().setData(body).setID(1).build();
        ChannelFuture cf = targetChannel.writeAndFlush(bu);
        cf.addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(" sent to client success");
            }
        });
        synchronized (targetChannel) {

            targetChannel.wait();
        }
        String ret = ResultHolder.get(targetChannel);
        System.out.println(ret);
        ByteBuf r = Unpooled.copiedBuffer(ret.getBytes());
        ctx.writeAndFlush(r);
    }
}
