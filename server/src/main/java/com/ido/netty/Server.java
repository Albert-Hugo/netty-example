package com.ido.netty;

import com.ido.example.codec.ProtoDecoder;
import com.ido.example.codec.ProtoEncoder;
import com.ido.netty.handler.*;
import com.ido.netty.proto.MyDataInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author Carl
 * @date 2019/12/23
 */
public class Server {

    public static void main(String[] args) throws InterruptedException {

        ServerBootstrap bootstrap = new ServerBootstrap();
        Bootstrap getEClient = new Bootstrap();
        EventLoopGroup master = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        EventLoopGroup clintLoop = new NioEventLoopGroup();
        EventLoopGroup bizGroup = new NioEventLoopGroup(100);
        try {

            bootstrap.group(master, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline
                                    .addLast(new HttpServerCodec())
                                    .addLast( new ProtoBufHandler());
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
            worker.shutdownGracefully();
            bizGroup.shutdownGracefully();
//            clintLoop.shutdownGracefully();
        }


    }
}
