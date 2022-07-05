package com.wheel.yun.cluster.loadbalance;

import com.wheel.yun.common.invoker.RpcInvocation;
import com.wheel.yun.invoker.WheelInvoker;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO 轮询算法
 * @date 2022/7/4 21:20
 *
 *
 * 计算服务提供者的总权重。
 * 每个服务提供者除了始终不变的固定权重以外，需要记录服务提供者当前权重。
 * 每次请求，更新所有的服务提供者的当前权重，当前权重 = 当前权重 + 固定权重。
 * 从上一步得到的结果中，选择一个当前权重最大的服务提供者用于处理请求，如果存在多个，那么就看遍历的过程中先遇到哪个服务提供者就是哪一个。并且，选中的权重最大的服务提供者更新其当前权重 = 当前权重 - 总权重
 *
 * 计算总权重 10+20+50 = 80
 * 当请求来了，更新三个服务提供者的当前权重，当前权重 = 当前权重+固定权重，依次结果为10，20，50.
 * 从中选择最大的一个，也就是第三个用于处理请求，同时将其当前权重更新：当前权重 = 50 - 80 = -30
 * 所以此时三个服务提供者的当前权重依次为10，20，-30
 * 当第二个请求来的时候，更新三个服务提供者的当前权重，当前权重 = 当前权重+固定权重，依次结果为20，40，20
 * 从重选择最大的一个，也就是第二个用于处理请求，同时将其当前权重更新：当前权重 = 40 - 80 = -40.
 * 依次类推。
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance{

    private static final int RECYCLE_PERIOD = 60000;

    protected static class WeightedRoundRobin {
        private int weight;
        private AtomicLong current = new AtomicLong(0);
        private long lastUpdate;

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
            current.set(0);
        }

        public long increaseCurrent() {
            return current.addAndGet(weight);
        }

        public void sel(int total) {
            current.addAndGet(-1 * total);
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }
    }
    private ConcurrentMap<String, ConcurrentMap<String, WeightedRoundRobin>> methodWeightMap = new ConcurrentHashMap<String, ConcurrentMap<String, WeightedRoundRobin>>();
    @Override
    protected WheelInvoker doSelect(List<WheelInvoker> invokers, RpcInvocation rpcInvocation) {
        String key = invokers.get(0).getIpAndPort()+"/"+getURL(rpcInvocation);
        ConcurrentMap<String, WeightedRoundRobin> map = methodWeightMap.computeIfAbsent(key, k -> new ConcurrentHashMap<>());
        int totalWeight = 0;
        long maxCurrent = Long.MIN_VALUE;
        long now = System.currentTimeMillis();
        WheelInvoker selectedInvoker = null;
        WeightedRoundRobin selectedWRR = null;
        for (WheelInvoker invoker : invokers) {
            String identifyString = invoker.getIpAndPort()+"/"+getURL(rpcInvocation);
            int weight = getWeight(invoker);
            WeightedRoundRobin weightedRoundRobin = map.computeIfAbsent(identifyString, k -> {
                WeightedRoundRobin wrr = new WeightedRoundRobin();
                wrr.setWeight(weight);
                return wrr;
            });

            if (weight != weightedRoundRobin.getWeight()) {
                //weight changed
                weightedRoundRobin.setWeight(weight);
            }
            long cur = weightedRoundRobin.increaseCurrent();
            weightedRoundRobin.setLastUpdate(now);
            if (cur > maxCurrent) {
                maxCurrent = cur;
                selectedInvoker = invoker;
                selectedWRR = weightedRoundRobin;
            }
            totalWeight += weight;
        }
        if (invokers.size() != map.size()) {
            map.entrySet().removeIf(item -> now - item.getValue().getLastUpdate() > RECYCLE_PERIOD);
        }
        if (selectedInvoker != null) {
            selectedWRR.sel(totalWeight);
            return selectedInvoker;
        }
        // should not happen here
        return invokers.get(0);
    }
}
