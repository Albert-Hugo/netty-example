package com.ido.netty;

import com.ido.netty.server.ProxyClient;

public class ClientStarter {
    public static void main(String[] args) {
        try {
            new ProxyClient().start(20002);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
