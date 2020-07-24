package com.ido.netty.manager;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TargetContextHolder {
    private final static Map<String, Channel> holder = new ConcurrentHashMap<>();



    public static void setMapping(String url, Channel ch) {
        holder.put(url, ch);
    }

    public static Channel getChannel(String url) {
        return holder.get(url);
    }

}
