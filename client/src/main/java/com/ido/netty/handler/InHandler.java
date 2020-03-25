package com.ido.netty.handler;

import com.ido.example.codec.ProtoMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Carl
 * @date 2019/12/23
 */
public class InHandler extends SimpleChannelInboundHandler<ProtoMsg> {
    Random random = new Random();
    private static Map<Integer ,String > randMsg = new HashMap<>();

    static {
        randMsg.put(0,"fagagasdfasdfag");
        randMsg.put(1,"agd");
        randMsg.put(2,"fagagasdfasdfa                      g");
        randMsg.put(3,"vvc");
        randMsg.put(4,"fagagasdfasd3243232323213fag");
        randMsg.put(5,"fdsagadsf");
        randMsg.put(6,"66666666");
        randMsg.put(7,"dsf");
        randMsg.put(8,"54652");
        randMsg.put(9,"fagagasdfa312312312sdfag");
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ProtoMsg msg = new ProtoMsg();
        int i = random.nextInt(10);
        msg.type = 1;
        msg.data = randMsg.get(i).getBytes();
        ctx.writeAndFlush(msg);


    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtoMsg msg) throws Exception {
        System.out.println(new String(msg.data, Charset.defaultCharset()));

    }


}
