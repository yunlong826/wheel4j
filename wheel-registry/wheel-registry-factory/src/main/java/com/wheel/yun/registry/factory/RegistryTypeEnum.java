package com.wheel.yun.registry.factory;

public enum RegistryTypeEnum {
    ZOOKEEPER((byte) 0),
    REDIS((byte) 1);

    private byte type;

    RegistryTypeEnum(byte type){
        this.type = type;
    }

    public byte getType() {
        return type;
    }


    public static RegistryTypeEnum parseByName(String typeName) {
        for (RegistryTypeEnum typeEnum : RegistryTypeEnum.values()) {
            if (typeEnum.name().equalsIgnoreCase(typeName)) {
                return typeEnum;
            }
        }
        return ZOOKEEPER;
    }

    public static RegistryTypeEnum parseByType(byte type) {
        for (RegistryTypeEnum typeEnum : RegistryTypeEnum.values()) {
            if (typeEnum.getType() == type) {
                return typeEnum;
            }
        }
        return ZOOKEEPER;
    }
}
