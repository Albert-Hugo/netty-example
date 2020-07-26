package com.ido.netty;

import com.ido.netty.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HttpProxyServer {

    public void start(int host) throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup master = new NioEventLoopGroup(100);
        EventLoopGroup worker = new NioEventLoopGroup(1000);
        try {

            bootstrap.group(master, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline
//                                    .addLast(new HttpServerCodec())
//                                    .addLast(new HttpResponseEncoder())
                                    .addLast(new ServerHandler());

                        }
                    });
            ChannelFuture f = bootstrap.bind("127.0.0.1", host);

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
            worker.shutdownGracefully();
        }
    }
}
