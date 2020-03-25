package com.ido.netty.client;

import com.ido.netty.ClientCallback;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Carl
 * @date 2020/3/16
 */
public class ClientContextHolder {

    public static ChannelHandlerContext GET_E_CONTEXT ;

    private static Map<String, Channel> channelMap = new ConcurrentHashMap<>();
    private static Map<String, ClientCallback> chanleCallback = new ConcurrentHashMap<>();



    public static void setClientChannel(Channel channel,ClientCallback cb){
        channelMap.put(channel.id().asShortText(),channel);
        chanleCallback.put(channel.id().asShortText(),cb);

    }

    public static ClientCallback getChannelCallback(Channel channel){
        return chanleCallback.get(channel.id().asShortText());

    }
}
