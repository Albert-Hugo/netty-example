package com.ido.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import lombok.extern.slf4j.Slf4j;

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
