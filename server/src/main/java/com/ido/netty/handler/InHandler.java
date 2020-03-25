package com.ido.netty.handler;

import com.ido.netty.ClientCallback;
import com.ido.netty.ClientConnector;
import com.ido.netty.client.ClientContextHolder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;

import java.io.File;
import java.net.URI;
import java.nio.charset.Charset;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.rtsp.RtspHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.rtsp.RtspResponseStatuses.UNAUTHORIZED;

/**
 * @author Carl
 * @date 2019/12/23
 */
public class InHandler extends SimpleChannelInboundHandler implements ClientCallback {


    static int count;
    private ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client ip: " + ctx.channel().remoteAddress());
        ;
        super.channelActive(ctx);
        this.ctx = ctx;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("client disconnect");
        ctx.channel().disconnect();

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpRequest request = (FullHttpRequest) msg;
        System.out.println(++count);


        System.out.println(((FullHttpRequest) msg).content().toString(Charset.defaultCharset()));
        //make a request to GET E server
        //get the channel connect to GET E server and send the request

        ClientContextHolder.GET_E_CONTEXT.writeAndFlush(request);

        makeGetERequest(ctx.channel(), request, (rsp) -> {


        });


        String res = "I am OK";
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                OK, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
        response.headers().set(CONTENT_TYPE, "text/plain");
        response.headers().set(CONNECTION, HttpHeaders.Values.CLOSE);
        response.headers().set(CONTENT_LENGTH,
                response.content().readableBytes());
//        if (request != null && HttpHeaders.isKeepAlive(request)) {
//            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
//        }
//        ClientConnector connector = new GetEHandler(ctx.channel());
//        connector.makeHttpRequest(request);
//
//        connector.writeHttpResult(response);


        Thread.sleep(1000 * 3);
        ctx.writeAndFlush(response);
//        future.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                if (future.isSuccess()) {
//                    System.out.println("close sucess!!");
//                }
//            }
//        });

    }

    private void makeGetERequest(Channel channel, FullHttpRequest request, ClientCallback clientCallback) {


        ClientContextHolder.setClientChannel(channel, clientCallback);
        ClientContextHolder.GET_E_CONTEXT.channel().writeAndFlush(request);


    }


    @Override
    public void afterHttpResponse(FullHttpResponse response) {
        //convert to response

        this.ctx.writeAndFlush(response);
    }
}
