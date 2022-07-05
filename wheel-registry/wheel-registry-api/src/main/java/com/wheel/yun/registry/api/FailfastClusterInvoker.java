package com.wheel.yun.registry.api;







import com.wheel.yun.cluster.factory.LoadBalanceFactory;
import com.wheel.yun.common.invoker.RpcInvocation;
import com.wheel.yun.invoker.WheelInvoker;

import java.util.List;

/**
 * 集群容灾策略，jdk代理直接使用
 * @Author: jessin
 * @Date: 19-11-25 下午10:13
 */
public class FailfastClusterInvoker {

    private RegistryDirectory registryDirectory;

    public FailfastClusterInvoker(RegistryDirectory registryDirectory) {
        this.registryDirectory = registryDirectory;
    }

    /**
     * 这里从RegistryDirectory获取invoker列表，选择一台进行调用，底层调用netty连接 抽取接口
     * todo 负载均衡算法，随机/轮询/加权，路由实现
     */
    public Object invoke(RpcInvocation rpcInvocation) {
        List<WheelInvoker> dubboInvokerList = registryDirectory.getInvokerList();
        if (dubboInvokerList.size() == 0) {
            throw new RuntimeException("no provider ");
        }
        WheelInvoker select = LoadBalanceFactory.getLoadBalance(dubboInvokerList.get(0).getLoadbalance()).select(dubboInvokerList, rpcInvocation);

        return select.invoke(rpcInvocation);
    }
}