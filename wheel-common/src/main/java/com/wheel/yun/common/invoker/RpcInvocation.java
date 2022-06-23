package com.wheel.yun.common.invoker;

import lombok.Data;

import java.lang.reflect.Method;

@Data
public class RpcInvocation {

    private String version;

    private String interfaceName;

    private String methodName;

    /**
     * 不序列化，consumer内部使用
     */
    private transient Method method;
    /**
     * 方法参数类型
     */
    private Class[] parameterType;

    private Object[] args;
    // todo attachment

}
