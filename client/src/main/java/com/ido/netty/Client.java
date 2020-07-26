package com.ido.netty;

import com.ido.netty.server.handler.AuthReqHandler;
import com.ido.netty.server.handler.InHandler;
import com.ido.netty.server.handler.ProtoBufHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author Carl
 * @date 2019/12/23
 */
@Slf4j
public class Client {


    public void connect() throws InterruptedException {
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
//                                    .addLast(new ProtoDecoder())
                                    .addLast(new IdleStateHandler(60,60,60))
                                    .addLast(new ProtobufVarint32LengthFieldPrepender())
                                    .addLast(new ProtobufEncoder())
                                    .addLast(new ProtoBufHandler());
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
            log.info("client shutdown");
            master.shutdownGracefully();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        new Client().connect();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Client().connect();


    }
}
