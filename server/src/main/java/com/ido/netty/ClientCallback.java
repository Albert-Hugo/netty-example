package com.ido.netty;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @author Carl
 * @date 2020/3/16
 */
public interface ClientCallback {

    /**
     *
     * @param response
     */
    void afterHttpResponse(FullHttpResponse response);

}
