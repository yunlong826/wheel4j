package com.wheel.yun.cluster.loadbalance;

import com.wheel.yun.common.invoker.RpcInvocation;
import com.wheel.yun.invoker.WheelInvoker;
import com.wheel.yun.rpc.common.RpcStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author jack_yun
 * @version 1.0
 * @description: 最少活跃负载均衡
 * @date 2022/7/4 19:03
 */
public class LeastActiveLoadBalance extends AbstractLoadBalance{
    @Override
    protected  WheelInvoker doSelect(List<WheelInvoker> invokers, RpcInvocation rpcInvocation) {
        int length = invokers.size();
        int leastActive = Integer.MAX_VALUE;
        int leastIndex = -1;
        int leastCount = 0;
        int[] weights = new int[length];
        for(int i = 0;i<length;i++){
            WheelInvoker invoker = invokers.get(i);
            int active = RpcStatus.getStatus(invoker.getIpAndPort(),rpcInvocation).getActive();
            weights[i] = active;
            if(leastActive>active){
                leastActive = active;
                leastIndex = i;
                leastCount = 1;
            }else if(leastActive == active){
                ++leastCount;
            }
        }
        if(leastCount == 1){
            return invokers.get(leastIndex);
        }
        List<Integer> weightLists = new ArrayList<>();
        for(int i = 0;i<weights.length;i++){
            if(weights[i] == leastActive)
                weightLists.add(i);
        }
        return invokers.get(ThreadLocalRandom.current().nextInt(weightLists.size()));

    }
}
