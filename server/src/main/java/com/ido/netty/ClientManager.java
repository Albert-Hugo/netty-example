package com.ido.netty;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Carl
 * @date 2020/3/27
 */
@Slf4j
public class ClientManager {
    private static Map<Long, Channel> clientChannels = new HashMap<>();


    public static void setClient(Long id, Channel channel) {
        clientChannels.put(id, channel);
    }

    public static Channel getClient(Long id) {
        return clientChannels.get(id);
    }
}