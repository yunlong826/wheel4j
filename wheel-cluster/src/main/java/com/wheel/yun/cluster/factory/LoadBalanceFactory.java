package com.wheel.yun.cluster.factory;

import com.wheel.yun.cluster.LoadBalance;
import com.wheel.yun.cluster.loadbalance.ConsistentHashLoadBalance;
import com.wheel.yun.cluster.loadbalance.LeastActiveLoadBalance;
import com.wheel.yun.cluster.loadbalance.RandomLoadBalance;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO 后续应该优化代码结构，该工厂模式过于简陋
 * @date 2022/7/4 21:52
 */
public class LoadBalanceFactory {
    public static LoadBalance getLoadBalance(String key){
        if(key.equals("random")){
            return new RandomLoadBalance();
        }else if(key.equals("consistentHash")){
            return new ConsistentHashLoadBalance();
        }else if(key.equals("leastActive")){
            return new LeastActiveLoadBalance();
        }else if(key.equals("roundRobin")){
            return new LeastActiveLoadBalance();
        }else{
            throw new IllegalArgumentException("no LoadBalance");
        }
    }
}
