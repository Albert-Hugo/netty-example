package com.ido.netty.handler;

import com.ido.example.codec.ProtoMsg;
import com.ido.netty.proto.MyDataInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.ido.example.codec.ProtoMsg.MSG_HEART_BEAT_PING;
import static com.ido.example.codec.ProtoMsg.MSG_HEART_BEAT_PONG;

/**
 * @author Carl
 * @date 2019/12/23
 */
@Slf4j
public class ProtoBufHandler extends SimpleChannelInboundHandler<ProtoMsg> {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ctx.writeAndFlush(MyDataInfo.MyMessage.newBuilder().setCat(MyDataInfo.Cat.newBuilder().setName("my cat"))
                .setDataType(MyDataInfo.MyMessage.DataType.CatType));


    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtoMsg msg) throws Exception {
        if (msg.type == MSG_HEART_BEAT_PONG) {
            log.info("server is still alive");
            return;
        }

        log.info(new String(msg.data, Charset.defaultCharset()));

    }


}
