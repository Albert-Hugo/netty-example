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
    public static byte MSG_HEART_BEAT = 4;
    public byte type;
    public long id;
    public byte[] data;

    public static long CLIENT_ID = 1L;//todo refactor as configurable


    public static ProtoMsg heartBeat() {
        ProtoMsg m = new ProtoMsg();
        m.type = MSG_HEART_BEAT;
        m.id = CLIENT_ID;
        return m;
    }


    public static ProtoMsg msg(String msg) {
        ProtoMsg m = new ProtoMsg();
        m.type = MSG;
        m.id = CLIENT_ID;
        m.data = msg.getBytes();
        return m;
    }

}
