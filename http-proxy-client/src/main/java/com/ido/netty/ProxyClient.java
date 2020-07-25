package com.ido.netty;

import com.ido.netty.handler.HttpHandler;
import com.ido.netty.handler.ProtoToHttpRequestHandler;
import com.ido.netty.proto.DataInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * 部署到客户本地的proxy client
 */
public class ProxyClient {
    public static void main(String[] args) throws InterruptedException {

        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup master = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {

            bootstrap.group(master, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline
                                    .addLast(new ProtobufVarint32FrameDecoder())
                                    .addLast(new ProtobufVarint32LengthFieldPrepender())
                                    .addLast(new ProtobufDecoder(DataInfo.testBuf.getDefaultInstance()))
                                    .addLast(new ProtobufEncoder())
                                    .addLast(new ProtoToHttpRequestHandler())
//                                    .addLast(new HttpRequestDecoder())
//                                    .addLast(new HttpObjectAggregator(5 * 1024))
//                                    .addLast(new HttpHandler())

                            ;
                        }
                    });
            ChannelFuture f = bootstrap.bind("127.0.0.1", 20002);

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
