package com.wheel.yun.cluster.loadbalance;

import com.wheel.yun.common.invoker.RpcInvocation;
import com.wheel.yun.invoker.WheelInvoker;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO 随机算法负载均衡
 * @date 2022/7/4 19:47
 *
 * 计算服务提供者的总权重，并维护一个前缀和数组。
 * 基于第一步的结果，在0～总权重之间生成一个随机数。
 * 随机数得到的一个权重，遍历前缀和数组，找到第一个比随机权重大的位置，即可确定选择的服务提供者。
 * 如果所有服务提供者节点都没有分配权重，或者分配的权重都一样，那么就采用随机的选择一个服务提供者节点。
 */
public class RandomLoadBalance extends AbstractLoadBalance{
    @Override
    protected  WheelInvoker doSelect(List<WheelInvoker> invokers, RpcInvocation rpcInvocation) {
        int length = invokers.size();
        boolean sameWeight = true;
        int[] weights = new int[length];
        int totalWeight = 0;
        for(int i = 0;i<length;i++){
            int weight = getWeight(invokers.get(i));
            totalWeight+=weight;
            weights[i] = totalWeight;
            if(sameWeight && totalWeight != weight*(i+1)){
                sameWeight = false;
            }
        }
        if(totalWeight > 0&&!sameWeight){
            int offset = ThreadLocalRandom.current().nextInt(totalWeight);
            for(int i = 0;i<length;i++){
                if(offset < weights[i]){
                    return invokers.get(i);
                }
            }
        }
        return invokers.get(ThreadLocalRandom.current().nextInt(length));
    }
}
