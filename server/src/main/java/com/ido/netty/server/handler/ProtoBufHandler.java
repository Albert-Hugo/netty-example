package com.ido.netty.server.handler;

import com.ido.example.codec.ProtoMsg;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Carl
 * @date 2019/12/23
 */
@Slf4j
public class ProtoBufHandler extends SimpleChannelInboundHandler<HttpObject> {
    static int count;
    private ChannelHandlerContext ctx;

    @AllArgsConstructor
    public static class ActiveEvent {
        String ip;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ctx.fireUserEventTriggered(new ActiveEvent(ctx.channel().remoteAddress().toString()));
        super.handlerAdded(ctx);
    }

    ;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("client ip: " + ctx.channel().remoteAddress());
        super.channelActive(ctx);
        this.ctx = ctx;

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            if (((IdleStateEvent) evt).state().equals(IdleState.READER_IDLE)) {
                log.info("指定时间内没有收到客户端请求");
                ctx.close();

            }
        }
        super.userEventTriggered(ctx, evt);


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


    private void handlerHeartbeat(ChannelHandlerContext ctx, ProtoMsg msg) {
        ctx.writeAndFlush(msg);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if(msg instanceof HttpRequest){
            HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK, Unpooled.copiedBuffer("test".getBytes()));
            response.headers().add("content-type","text/plain");
            response.headers().add("content-length",Unpooled.copiedBuffer("test".getBytes()).readableBytes());
            ctx.writeAndFlush(response);


        }


    }
}
