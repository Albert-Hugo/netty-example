package com.ido.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @author Carl
 * @date 2020/3/16
 */
public interface ClientConnector {
    void makeHttpRequest(FullHttpRequest req);


    void writeHttpResult(FullHttpResponse fullHttpResponse);


}
