package com.ido.netty.handler;

import com.ido.netty.proto.DataInfo;
import com.ido.util.HttpUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObject;

import java.util.HashMap;
import java.util.Map;

/**
 * http 协议透传
 */
public class HttpHandler extends SimpleChannelInboundHandler<HttpObject> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
//        if (msg instanceof FullHttpRequest) {
//            FullHttpRequest req = (FullHttpRequest) msg;
//            byte[] data = new byte[req.content().readableBytes()];
//            req.content().getBytes(0, data);
//            Map<String, String> headers = new HashMap<>();
//            for (Map.Entry<String, String> entry : req.headers().entries()) {
//                headers.put(entry.getKey(), entry.getValue());
//            }
//
//            byte[] result = null;
//
//            if (req.method().name().equals("POST")) {
//            } else {
//                result = HttpUtil.get("http://localhost:8081" + req.getUri(), headers);
//            }
//            System.out.println(new String(result));
//            DataInfo.testBuf ret = DataInfo.testBuf.newBuilder().setID(1).setData(new String(result)).build();
//            ctx.writeAndFlush(ret);
//        }

    }
}
