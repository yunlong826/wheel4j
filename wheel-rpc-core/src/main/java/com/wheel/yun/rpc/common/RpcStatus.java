package com.wheel.yun.rpc.common;

import com.wheel.yun.common.invoker.RpcInvocation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/7/4 19:20
 */
public class RpcStatus {

    private static final ConcurrentMap<String, RpcStatus> SERVICE_STATISTICS = new ConcurrentHashMap<String,
            RpcStatus>();

    private static final ConcurrentMap<String, ConcurrentMap<String, RpcStatus>> METHOD_STATISTICS =
            new ConcurrentHashMap<String, ConcurrentMap<String, RpcStatus>>();

    private final AtomicInteger active = new AtomicInteger();

    /**
     * get active.
     *
     * @return active
     */
    public int getActive() {
        return active.get();
    }


    public static boolean beginCount(String ipAndPort,RpcInvocation rpcInvocation, int max) {
        String url = ipAndPort+"/"+rpcInvocation.getInterfaceName()+"/"+rpcInvocation.getMethodName()+"/"+rpcInvocation.getMethod();
        max = (max <= 0) ? Integer.MAX_VALUE : max;
        RpcStatus appStatus = getStatus(url);
        RpcStatus methodStatus = getStatus(ipAndPort,rpcInvocation);
        if (methodStatus.active.get() == Integer.MAX_VALUE) {
            return false;
        }
        for (int i; ; ) {
            i = methodStatus.active.get();

            if (i == Integer.MAX_VALUE || i + 1 > max) {
                return false;
            }

            if (methodStatus.active.compareAndSet(i, i + 1)) {
                break;
            }
        }

        appStatus.active.incrementAndGet();

        return true;
    }
    public static RpcStatus getStatus(String uri) {
        return SERVICE_STATISTICS.computeIfAbsent(uri, key -> new RpcStatus());
    }

    public static RpcStatus getStatus(String ipAndPort,RpcInvocation rpcInvocation){
        String uri = ipAndPort+"/"+rpcInvocation.getInterfaceName()+"/"+rpcInvocation.getMethodName()+"/"+rpcInvocation.getMethod();
        ConcurrentMap<String, RpcStatus> map = METHOD_STATISTICS.computeIfAbsent(uri, k -> new ConcurrentHashMap<>());
        return map.computeIfAbsent(rpcInvocation.getMethodName(), k -> new RpcStatus());
    }
}
