package com.ido.netty.handler;

import com.ido.netty.proto.DataInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 将protobuf 转化为http raw request
 */
public class ProtoToHttpRequestHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DataInfo.testBuf data = (DataInfo.testBuf) msg;

        Socket socket = new Socket("127.0.0.1", 8081);
        //获取输出流，向服务器端发送信息
        OutputStream outputStream = socket.getOutputStream();//字节输出流
        PrintWriter pw = new PrintWriter(outputStream); //将输出流包装为打印流
        pw.write(data.getData());
        pw.flush();
        socket.shutdownOutput();

        //获取输入流，读取服务器端的响应
        InputStream inputStream = socket.getInputStream();
        InputStream br = new BufferedInputStream(inputStream);
        byte[] bs = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (-1 != br.read(bs)) {
            sb.append(new String(bs));

        }
        socket.shutdownInput();

        //关闭资源
        br.close();
        inputStream.close();
        pw.close();
        outputStream.close();
        socket.close();
        DataInfo.testBuf testBuf = DataInfo.testBuf.newBuilder().setID(1).setData(sb.toString()).build();
        ctx.writeAndFlush(testBuf);
//        super.channelRead(ctx, Unpooled.copiedBuffer(data.getData().getBytes()));
    }
}