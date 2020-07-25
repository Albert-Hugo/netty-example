package com.ido.netty.client.handler;

import com.ido.netty.manager.ResultHolder;
import com.ido.netty.proto.DataInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.net.URI;

public class ClientHandler extends SimpleChannelInboundHandler<DataInfo.testBuf> {
    private Channel proxyServerChannel;

    public ClientHandler(Channel proxyServerChannel) {
        this.proxyServerChannel = proxyServerChannel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("connect to remote proxy client");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataInfo.testBuf msg) throws Exception {
        //todo 将这里的结果返回到 proxy server里面
        ByteBuf r = Unpooled.copiedBuffer(msg.getData().getBytes());
        ResultHolder.get(channelHandlerContext.channel()).writeAndFlush(r);

    }
}
