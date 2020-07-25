package com.ido.netty.manager;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResultHolder {
    final static Map<Channel, String> holder = new ConcurrentHashMap<>();

    public static String get(Channel ch) {
        return holder.get(ch);
    }


    public static void put(Channel ch, String result) {
        holder.put(ch, result);
    }
}
