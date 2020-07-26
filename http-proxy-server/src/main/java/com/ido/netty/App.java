package com.ido.netty;

import com.ido.netty.client.ClientProxyConnector;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    private static CountDownLatch countDownLatch  = new CountDownLatch(2);

    public static void main(String[] args) throws InterruptedException {


        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(()->{
            try {
                new HttpProxyServer().start(20001);//start http server and listening
                countDownLatch.countDown();;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        executorService.execute(()->{
            try {
                new ClientProxyConnector().connect(20002);
                countDownLatch.countDown();;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        countDownLatch.await();
        executorService.shutdown();

    }
}
