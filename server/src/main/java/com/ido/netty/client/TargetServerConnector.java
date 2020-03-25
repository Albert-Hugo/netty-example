package com.ido.netty.client;

import com.ido.netty.handler.InHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author Carl
 * @date 2019/12/23
 */
public class TargetServerConnector {

    public static void main(String[] args) throws InterruptedException {

        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup master = new NioEventLoopGroup();
        try {

            bootstrap.group(master)
                    .channel(NioServerSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline
//                                    .addLast(new HttpResponseEncoder())
                                    .addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(512 * 1024))
                                    .addLast(new InHandler());
                        }
                    });
            ChannelFuture f = bootstrap.bind("127.0.0.1", 8080);

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
            master.shutdownGracefully();
        }


    }
}
