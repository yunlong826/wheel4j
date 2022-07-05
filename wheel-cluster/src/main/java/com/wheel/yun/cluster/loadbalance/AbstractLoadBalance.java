package com.wheel.yun.cluster.loadbalance;

import com.wheel.yun.cluster.LoadBalance;
import com.wheel.yun.common.invoker.RpcInvocation;
import com.wheel.yun.invoker.WheelInvoker;

import java.util.List;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/7/4 16:58
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public <T> WheelInvoker select(List<WheelInvoker> invokers, RpcInvocation rpcInvocation) {
        if(invokers == null || invokers.size() == 0)
            return null;
        if(invokers.size() == 1)
            return invokers.get(0);
        return doSelect(invokers,rpcInvocation);
    }
    public String getURL(RpcInvocation rpcInvocation){
        return rpcInvocation.getInterfaceName()+"/"+rpcInvocation.getMethodName()+"/"+rpcInvocation.getVersion();
    }
    public Integer getWeight(WheelInvoker wheelInvoker){
        return wheelInvoker.getWeight();
    }
    protected abstract  WheelInvoker doSelect(List<WheelInvoker> invokers,RpcInvocation rpcInvocation);
}
