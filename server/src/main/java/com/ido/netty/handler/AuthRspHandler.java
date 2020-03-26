package com.ido.netty.handler;

import com.ido.example.codec.ProtoMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Arrays;
import java.util.List;

import static com.ido.example.codec.ProtoMsg.AUTH_REQ;
import static com.ido.example.codec.ProtoMsg.AUTH_RSP_FAILED;
import static com.ido.example.codec.ProtoMsg.AUTH_RSP_SUCCESS;

/**
 * @author Carl
 * @date 2019/12/23
 */
public class AuthRspHandler extends ChannelInboundHandlerAdapter {

    private static List<String> whiteList = Arrays.asList("192.168.1.172");

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("client disconnect");

    }


    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        ProtoMsg msg = (ProtoMsg) o;
        if (msg.type == AUTH_REQ) {//auth 类型
            ProtoMsg rsp = new ProtoMsg();
            if (whiteList.contains(channelHandlerContext.channel().remoteAddress().toString())) {
                rsp.type = AUTH_RSP_SUCCESS;
            }else{
                rsp.type = AUTH_RSP_FAILED;
            }

            channelHandlerContext.writeAndFlush(rsp);
        }

    }


}
