package com.ido.netty.handler;

import com.ido.example.codec.ProtoMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import static com.ido.example.codec.ProtoMsg.AUTH_REQ;
import static com.ido.example.codec.ProtoMsg.AUTH_RSP_FAILED;
import static com.ido.example.codec.ProtoMsg.CLIENT_ID;

/**
 * @author Carl
 * @date 2019/12/23
 */
@Slf4j
public class AuthReqHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ProtoMsg authMsg = new ProtoMsg();
        authMsg.type = AUTH_REQ;
        String user = "ido";
        String psw = "666";
        authMsg. id = CLIENT_ID;
        authMsg.data = (user + ":" + psw).getBytes();
        ctx.writeAndFlush(authMsg);


    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);

        ProtoMsg authRsp = (ProtoMsg) msg;

        if (authRsp.type == AUTH_RSP_FAILED) {
            log.info("authentication fail!!!");
        }


    }
}
