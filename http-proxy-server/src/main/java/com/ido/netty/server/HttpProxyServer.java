package com.ido.netty.server;

import com.ido.netty.server.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpProxyServer {

    public void start(int port) throws InterruptedException {
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
                                    .addLast(new ServerHandler());

                        }
                    });
            ChannelFuture f = bootstrap.bind("127.0.0.1", port);

            f
                    .addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            if (!future.isSuccess()) {
                                future.cause().printStackTrace();
                            }

                            log.info("proxy server started and listen at "+ port);


                        }
                    });


            f.channel().closeFuture().sync();


        } finally {
            master.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
