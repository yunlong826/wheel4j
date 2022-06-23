package com.wheel.yun.rpc.common;




import com.wheel.yun.common.invoker.RpcInvocation;
import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: changjiu.wang
 * @Date: 2021/7/24 22:55
 */
@Data
public class RpcRequest implements Serializable {

    private static AtomicLong idGenerator = new AtomicLong();

    /**
     * 每个请求生成唯一的id
     */
    private long id = idGenerator.incrementAndGet();

    /**
     * 数据
     */
    private RpcInvocation rpcInvocation;

    private boolean isEvent;

//    /**
//     * 请求的服务名 + 版本
//     */
//    private String serviceName;
//    /**
//     * 请求调用的方法
//     */
//    private String method;
//
//    /**
//     *  参数类型
//     */
//    private Class<?>[] parameterTypes;
//
//    /**
//     *  参数
//     */
//    private Object[] parameters;
//
//    public String getServiceName() {
//        return serviceName;
//    }
//
//    public void setServiceName(String serviceName) {
//        this.serviceName = serviceName;
//    }
//
//    public String getMethod() {
//        return method;
//    }
//
//    public void setMethod(String method) {
//        this.method = method;
//    }
//
//    public Class<?>[] getParameterTypes() {
//        return parameterTypes;
//    }
//
//    public void setParameterTypes(Class<?>[] parameterTypes) {
//        this.parameterTypes = parameterTypes;
//    }
//
//    public Object[] getParameters() {
//        return parameters;
//    }
//
//    public void setParameters(Object[] parameters) {
//        this.parameters = parameters;
//    }
}
