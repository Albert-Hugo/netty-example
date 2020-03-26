package com.ido.netty;

import com.ido.example.codec.ProtoDecoder;
import com.ido.example.codec.ProtoEncoder;
import com.ido.netty.handler.AuthReqHandler;
import com.ido.netty.handler.InHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author Carl
 * @date 2019/12/23
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {

        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup master = new NioEventLoopGroup();
        try {

            bootstrap.group(master)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline
                                    .addLast(new ProtoDecoder())
                                    .addLast(new ProtoEncoder())
                                    .addLast(new AuthReqHandler())
                                    .addLast(new InHandler());
                        }
                    });
            ChannelFuture f = bootstrap.connect("127.0.0.1", 20001);

            f
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (!future.isSuccess()) {
                                future.cause().printStackTrace();
                            }


                        }
                    });


            f.channel().closeFuture().sync();

        } finally {
            System.out.println("client shutdown");
            master.shutdownGracefully();
        }


    }
}
