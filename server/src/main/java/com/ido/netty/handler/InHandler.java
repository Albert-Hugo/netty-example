package com.ido.netty.handler;

import com.ido.example.codec.ProtoMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

import static com.ido.example.codec.ProtoMsg.MSG_HEART_BEAT_PING;
import static com.ido.example.codec.ProtoMsg.MSG_HEART_BEAT_PONG;

/**
 * @author Carl
 * @date 2019/12/23
 */
@Slf4j
public class InHandler extends SimpleChannelInboundHandler<ProtoMsg> {
    static int count;
    private ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("client ip: " + ctx.channel().remoteAddress());
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
        log.info("client disconnect");
        ctx.channel().disconnect();

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtoMsg msg) throws Exception {
        if (msg.type == MSG_HEART_BEAT_PING) {
            ProtoMsg hb = ProtoMsg.heartBeatPong();
            handlerHeartbeat(ctx, hb);
            return;
        }
        log.info("client say : " + new String(msg.data, Charset.defaultCharset()));

        ProtoMsg rsp = new ProtoMsg();
        rsp.type = 1;
        rsp.data = "echo from server".getBytes();
        ctx.writeAndFlush(rsp);


    }

    private void handlerHeartbeat(ChannelHandlerContext ctx, ProtoMsg msg) {
        ctx.writeAndFlush(msg);
    }

}
