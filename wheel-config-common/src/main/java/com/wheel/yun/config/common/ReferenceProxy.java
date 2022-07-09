package com.wheel.yun.config.common;


import com.wheel.yun.common.config.InterfaceConfig;
import com.wheel.yun.common.invoker.RpcInvocation;
import com.wheel.yun.registry.api.FailfastClusterInvoker;
import com.wheel.yun.registry.api.RegistryDirectory;
import com.wheel.yun.rpc.common.RpcResponse;
import com.wheel.yun.rpc.common.RpcStatus;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/6/5 1:34
 */
@Slf4j
public class ReferenceProxy<T> implements InvocationHandler {

    private AtomicBoolean destroyed = new AtomicBoolean();

    private String clazzName;

    private Class<T> clazz;

    private String interfaceName;

    private Object proxy;

    private RegistryDirectory registryDirectory;

    private FailfastClusterInvoker failfastClusterInvoker;

    private InterfaceConfig interfaceConfig;

    private String ipAndPort;




    public ReferenceProxy(Class<T> clazz, InterfaceConfig interfaceConfig,RegistryConfig registryConfig){
        proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz}, this);
        this.clazzName = clazz.getName();
        this.ipAndPort = registryConfig.getHost()+":"+registryConfig.getPort();
        registryDirectory = new RegistryDirectory(clazzName, this.ipAndPort
                ,interfaceConfig);
        failfastClusterInvoker = new FailfastClusterInvoker(registryDirectory);
        this.interfaceConfig = interfaceConfig;
    }

    public Object getProxy() {
        return proxy;
    }

    public void destroy() {
        if (destroyed.compareAndSet(false, true)) {
            registryDirectory.destroy();
        }
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 销毁后不可用
        if (destroyed.get()) {
            throw new RuntimeException("RerferenceProxy已经被销毁，不可使用");
        }

        if ("toString".equals(method.getName())) {
            return this.toString();
        }
        // todo group，attachment
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setInterfaceName(clazzName);
        rpcInvocation.setMethod(method);
        rpcInvocation.setParameterType(method.getParameterTypes());
        rpcInvocation.setArgs(args);
        rpcInvocation.setMethodName(method.getName());
        rpcInvocation.setVersion(interfaceConfig.getVersion());
        Class returnType = method.getReturnType();

        // todo 如果本地与实现类，且版本、group均匹配的话，直接调用本地的
        log.info("jdk调用：{}，代理类为：{}，返回类型：{}", rpcInvocation, proxy, returnType);
        // todo 通过接口配置决定用哪种策略
        RpcResponse response = (RpcResponse)failfastClusterInvoker.invoke(rpcInvocation);
        // 发起了一次调用，被调用的服务在后台被计数一次，配合wheel-cluster中LeastActiveLoadBalance类使用
        RpcStatus.beginCount(ipAndPort,rpcInvocation,Integer.MAX_VALUE);
        log.info("-------------------------->ipAndPort:{},jdk调用:{},activeCount:{}",ipAndPort,rpcInvocation,
                RpcStatus.getStatus(ipAndPort,rpcInvocation).getActive());
        if (returnType == Void.class) {
            return null;
        }
        return response.getResult();
    }
}
