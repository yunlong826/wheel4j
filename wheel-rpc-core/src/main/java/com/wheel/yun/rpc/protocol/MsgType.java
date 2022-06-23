package com.wheel.yun.rpc.protocol;



public enum MsgType {
    REQUEST((byte) 1),
    RESPONSE((byte) 2);


    private byte type;

    public byte getType(){
        return type;
    }

    MsgType(byte type) {
        this.type = type;
    }

    public static MsgType findByType(byte type) {
        for (MsgType msgType : MsgType.values()) {
            if (msgType.getType() == type) {
                return msgType;
            }
        }
        return null;
    }
}
