package com.ido.netty.handler;

import com.ido.example.codec.ProtoMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static com.ido.example.codec.ProtoMsg.AUTH_REQ;
import static com.ido.example.codec.ProtoMsg.AUTH_RSP_FAILED;

/**
 * @author Carl
 * @date 2019/12/23
 */
public class AuthReqHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ProtoMsg authMsg = new ProtoMsg();
        authMsg.type = AUTH_REQ;
        authMsg.data = "".getBytes();
        ctx.writeAndFlush(authMsg);


    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);

        ProtoMsg authRsp = (ProtoMsg) msg;

        if(authRsp.type == AUTH_RSP_FAILED){
            System.out.println("authentication fail!!!");
        }

    }
}