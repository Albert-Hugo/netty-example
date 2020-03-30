package com.ido.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author Carl
 * @date 2019/12/23
 */
@Slf4j
public class OutHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ChannelPromise channelPromise = new DefaultChannelPromise(ctx.channel());
        channelPromise.addListener((future -> {
            if(!future.isSuccess()){
                log.info(future.cause().toString());
            }
        }));
        super.write(ctx, msg, channelPromise);
    }
}
