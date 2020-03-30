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
    public static byte MSG_HEART_BEAT_PING = 4;
    public static byte MSG_HEART_BEAT_PONG = 5;
    public byte type;
    public long id;
    public byte[] data;
    private final static ProtoMsg PONG;
    static {
        PONG = new ProtoMsg();
        PONG.type = MSG_HEART_BEAT_PONG;
    }

    public static long CLIENT_ID = 1L;//todo refactor as configurable


    public static ProtoMsg heartBeat() {
        ProtoMsg m = new ProtoMsg();
        m.type = MSG_HEART_BEAT_PING;
        m.id = CLIENT_ID;
        return m;
    }

    public static ProtoMsg heartBeatPong() {
        return PONG;
    }

    public static ProtoMsg msg(String msg) {
        ProtoMsg m = new ProtoMsg();
        m.type = MSG;
        m.id = CLIENT_ID;
        m.data = msg.getBytes();
        return m;
    }

}
