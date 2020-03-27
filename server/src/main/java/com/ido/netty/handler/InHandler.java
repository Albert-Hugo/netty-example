package com.ido.netty.handler;

import com.ido.example.codec.ProtoMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

import static com.ido.example.codec.ProtoMsg.MSG_HEART_BEAT;

/**
 * @author Carl
 * @date 2019/12/23
 */
public class InHandler extends SimpleChannelInboundHandler<ProtoMsg> {


    static int count;
    private ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client ip: " + ctx.channel().remoteAddress());
        ;
        super.channelActive(ctx);
        this.ctx = ctx;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("client disconnect");
        ctx.channel().disconnect();

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtoMsg msg) throws Exception {

        if(msg.type == MSG_HEART_BEAT){
            handlerHeartbeat(ctx,msg);
            return ;
        }
        System.out.println("client say : " + new String(msg.data, Charset.defaultCharset()));

        ProtoMsg rsp = new ProtoMsg();
        rsp.type = 1;
        rsp.data = "echo from server".getBytes();
        ctx.writeAndFlush(rsp);


    }

    private void handlerHeartbeat(ChannelHandlerContext ctx,ProtoMsg msg) {
        ctx.writeAndFlush(msg);
    }

}
