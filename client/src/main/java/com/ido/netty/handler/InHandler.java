package com.ido.netty.handler;

import com.ido.example.codec.ProtoMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.ido.example.codec.ProtoMsg.MSG_HEART_BEAT;

/**
 * @author Carl
 * @date 2019/12/23
 */
public class InHandler extends SimpleChannelInboundHandler<ProtoMsg> {
    Random random = new Random();
    private static Map<Integer, String> randMsg = new HashMap<>();

    private static class HeartbaetWorker implements Runnable {
        private ChannelHandlerContext ctx;
        int retryTime = 0;

        public HeartbaetWorker(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            while (true) {
                long sleepTime;
                if (retryTime >= 10) {
                    sleepTime = 1000 * 10;
                    //todo mark the server as down;
                } else {
                    sleepTime = 1000 * (retryTime + 1);
                }
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                retryTime++;
                System.out.println("retry to ping server " + retryTime + "times");
                ProtoMsg msg = new ProtoMsg();
                msg.type = MSG_HEART_BEAT;
                ctx.writeAndFlush(msg);


            }
        }
    }

    static {
        randMsg.put(0, "fagagasdfasdfag");
        randMsg.put(1, "agd");
        randMsg.put(2, "fagagasdfasdfa                      g");
        randMsg.put(3, "vvc");
        randMsg.put(4, "fagagasdfasd3243232323213fag");
        randMsg.put(5, "fdsagadsf");
        randMsg.put(6, "66666666");
        randMsg.put(7, "dsf");
        randMsg.put(8, "54652");
        randMsg.put(9, "fagagasdfa312312312sdfag");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        new Thread(new HeartbaetWorker(ctx)).start();


    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ProtoMsg msg) throws Exception {
        if (msg.type == MSG_HEART_BEAT) {
            System.out.println("server is still alive");
            return;
        }

        System.out.println(new String(msg.data, Charset.defaultCharset()));

    }


}
