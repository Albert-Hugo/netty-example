package com.ido.netty.client.handler;

import com.ido.netty.manager.ResultHolder;
import com.ido.netty.proto.DataInfo;
import com.sun.org.apache.xpath.internal.operations.String;
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
        System.out.println("connect to remote proxy client");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataInfo.testBuf msg) throws Exception {
//        System.out.println("收到代理返回的结果：" +  msg.getData());
        synchronized (channelHandlerContext.pipeline().channel()){
            channelHandlerContext.pipeline().channel().notify();
        }
        ResultHolder.put(channelHandlerContext.pipeline().channel(),msg.getData());
        //todo 将这里的结果返回到 proxy server里面
    }
}
