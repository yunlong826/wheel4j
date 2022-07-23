package com.wheel.yun.cluster;

import com.wheel.yun.common.invoker.RpcInvocation;
import com.wheel.yun.invoker.WheelInvoker;

import java.util.List;

/**
 * @author jack_yun
 * @version 1.0
 * @description:  负载均衡接口
 * @date 2022/7/4 16:52
 */
public interface LoadBalance {
    <T> WheelInvoker select(List<WheelInvoker> invokers,RpcInvocation rpcInvocation);
}
