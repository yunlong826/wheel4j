package com.wheel.serial.api;



/**
 * @Author: changjiu.wang
 * @Date: 2021/7/25 5:28
 */
public enum SerializationTypeEnum {

    HESSIAN((byte) 0),
    JSON((byte) 1);


    private byte type;

    public byte getType(){
        return type;
    }

    SerializationTypeEnum(byte type) {
        this.type = type;
    }

    public static SerializationTypeEnum parseByName(String typeName) {
        for (SerializationTypeEnum typeEnum : SerializationTypeEnum.values()) {
            if (typeEnum.name().equalsIgnoreCase(typeName)) {
                return typeEnum;
            }
        }
        return HESSIAN;
    }

    public static SerializationTypeEnum parseByType(byte type) {
        for (SerializationTypeEnum typeEnum : SerializationTypeEnum.values()) {
            if (typeEnum.getType() == type) {
                return typeEnum;
            }
        }
        return HESSIAN;
    }

}
