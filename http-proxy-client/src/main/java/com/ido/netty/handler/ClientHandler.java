package com.ido.netty.handler;

import com.ido.netty.proto.DataInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<DataInfo.testBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataInfo.testBuf msg) throws Exception {
        System.out.println(msg.getData());
    }
}
