package com.ido.netty.client.handler;

import com.ido.netty.proto.DataInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.net.URI;

public class ClientHandler extends SimpleChannelInboundHandler<DataInfo.testBuf> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("connect to remote proxy server");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataInfo.testBuf msg) throws Exception {
        
    }
}
