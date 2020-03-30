package com.ido.netty.handler;

import com.ido.example.codec.ProtoMsg;
import com.ido.netty.ClientManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import static com.ido.example.codec.ProtoMsg.*;

/**
 * @author Carl
 * @date 2019/12/23
 */
@Slf4j
public class AuthRspHandler extends ChannelInboundHandlerAdapter {

    private static List<String> whiteList = Arrays.asList("192.168.1.172", "127.0.0.1");

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("client disconnect");

    }


    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        ProtoMsg msg = (ProtoMsg) o;
        if (msg.type == AUTH_REQ) {//auth 类型
            ProtoMsg rsp = new ProtoMsg();
            String ip = channelHandlerContext.channel().remoteAddress().toString().split(":")[0];
            ip = ip.substring(1);
            if (whiteList.contains(ip)) {
                String[] authData = new String(msg.data, Charset.defaultCharset()).split(":");
                if (authData.length != 2 || !authData[0].equals("ido") || !authData[1].equals("666")) {
                    log.info("auth data not right!");
                    rsp.type = AUTH_RSP_FAILED;
                } else {
                    //登录验证成功
                    rsp.type = AUTH_RSP_SUCCESS;
                    //保存 client 的 channel
                    ClientManager.setClient(msg.id, channelHandlerContext.channel());
                }

            } else {
                log.info("remote address " + ip + "not in white list");
                rsp.type = AUTH_RSP_FAILED;
            }

            channelHandlerContext.writeAndFlush(rsp);
        } else {

            if (msg.type == MSG && ClientManager.getClient(msg.id) == null) {
                //如果没有认证通过的连接，直接丢弃
                log.info("client not auth  " + msg.id);
                channelHandlerContext.close();
                return;
            }
            channelHandlerContext.fireChannelRead(o);
        }


    }


}
