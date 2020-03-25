package com.ido.example.codec;

/**
 * @author Carl
 * @date 2020/3/25
 */

public class ProtoMsg {
    public static byte MSG = 1;
    public static byte AUTH_REQ = 0;
    public static byte AUTH_RSP_SUCCESS = 2;
    public static byte AUTH_RSP_FAILED = 3;
    public byte type;
    public byte[] data;

}
