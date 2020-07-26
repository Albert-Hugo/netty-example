package com.ido.netty.server.handler;

import com.ido.netty.client.ClientContextHolder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @author Carl
 * @date 2020/3/16
 */
public class GetEHandler extends SimpleChannelInboundHandler {
    private Channel channel;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ClientContextHolder.GET_E_CONTEXT = ctx;

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpResponse rsp = (FullHttpResponse) msg;
        //匹配到原来的client channel
        ClientContextHolder.getChannelCallback(ctx.channel()).afterHttpResponse(rsp);


    }

}
