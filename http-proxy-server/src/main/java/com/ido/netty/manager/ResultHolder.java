package com.ido.netty.manager;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResultHolder {
    final static Map<Channel, Channel> holder = new ConcurrentHashMap<>();

    public static Channel get(Channel ch) {
        return holder.get(ch);
    }


    public static void put(Channel ch, Channel result) {
        holder.put(ch, result);
    }
}
