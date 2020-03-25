package com.ido;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;


/**
 * @author Carl
 * @date 2020/3/13
 */
public class TestClient {


    public static void main(String[] args) {
        // The final survival time also depends on the server's keep-alive settings, idle time, and intermittent validation.
        PoolingHttpClientConnectionManager pool = new PoolingHttpClientConnectionManager(1, TimeUnit.SECONDS);
        pool.setMaxTotal(50);
        // At present, there is only one route, which is equal to the maximum by default. It is set according to the traffic concurrency.
        pool.setDefaultMaxPerRoute(50);
        //Check the inactive connection to avoid the failure caused by the active closing of the connection after the server restarts or the keep alive expires. It may be common for the microservice scenario, but it is limited by the HTTP design concept, which is also completely reliable. Use the re try / re execute mechanism to make up for this. Considering that many niginx may be configured to keep alive for 5 seconds.
        pool.setValidateAfterInactivity(5 * 1000);


        HttpGet httpRequest = new HttpGet("test/id=falgjldaflgdsgsadf");
        for (int i = 0; i < 100; i++) {
            HttpClient client = HttpClients.custom()
                    .setConnectionManager(pool)
                    // Recovery occurs when the connection is idle for 10 seconds. This will start independent thread detection, so the destroy method must be declared to close the independent thread.
                    .evictIdleConnections(1, TimeUnit.SECONDS)
                    //Establish connection time, get connection time from connection pool, and data transfer time
                    .setDefaultRequestConfig(RequestConfig.custom()
                            // HTTP connection establishment timeout
                            .setConnectTimeout(3000)
                            // Getting connection timeout from connection pool
                            .setConnectionRequestTimeout(5000)
                            // socket timeout
                            .setSocketTimeout(10000)
                            .build())
                    //Custom retry mechanism
                    .setRetryHandler((exception, executionCount, context) -> {
                        // At present, only one retry is allowed.
                        if (executionCount > 1) {
                            return false;
                        }
                        // If the server actively closes the connection, the data is not accepted by the server and can be retried.
                        if (exception instanceof NoHttpResponseException) {
                            return true;
                        }
                        //Do not retry SSL handshake exception
                        if (exception instanceof SSLHandshakeException) {
                            return false;
                        }
                        // timeout
                        if (exception instanceof InterruptedIOException) {
                            return false;
                        }
                        //Target server unreachable
                        if (exception instanceof UnknownHostException) {
                            return false;
                        }
                        // Exceptional handshake in SSL
                        if (exception instanceof SSLException) {
                            return false;
                        }
                        HttpClientContext clientContext = HttpClientContext.adapt(context);
                        HttpRequest request = clientContext.getRequest();
                        String get = "GET";
                        // The GET method is idempotent and can be retried
                        if (request.getRequestLine().getMethod().equalsIgnoreCase(get)) {
                            return true;
                        }
                        return false;
                    })
                    //The default connectionkeepalivestrategy is dynamically calculated based on keep alive
                    .build();
            try {
                CloseableHttpResponse response = (CloseableHttpResponse) client.execute(new HttpHost("127.0.0.1", 20001), httpRequest);
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
