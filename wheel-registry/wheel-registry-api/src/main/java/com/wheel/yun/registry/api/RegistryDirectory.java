package com.wheel.yun.registry.api;

import com.google.common.collect.Lists;

import com.wheel.yun.common.config.InterfaceConfig;
import com.wheel.yun.invoker.WheelInvoker;
import com.wheel.yun.registry.comm.ChildListener;
import com.wheel.yun.registry.comm.RegistryService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 注册项目录，每个service的所有的dubboInvoker都保存到这里，实现zk listener，监听zk路径变化，当zk变化时，构造WheelInvoker。
 * 同一个zk集群同一台机器应该只建立一个共享连接
 * @Author: jessin
 * @Date: 19-11-25 下午10:34
 */
@Slf4j
public class RegistryDirectory implements ChildListener {

    private Map<String, WheelInvoker> ipAndPortAndWeight2InvokerMap = new ConcurrentHashMap<>();

    private RegistryService registryService;

    private InterfaceConfig interfaceConfig;

    private String providerPath;

    private String registryAddress;

    /**
     * 创建zk连接，监听zk路径创建DubboInvoker
     * @param path
     */
    public RegistryDirectory(String path, String registry, InterfaceConfig interfaceConfig) {
        this.interfaceConfig = interfaceConfig;
        this.registryAddress = registry;
        // 监听group/接口名/providers，有变化时通知RegistryDirectory，也就是调用notify(url, listener, urls);
        this.providerPath = "/wheel/"+interfaceConfig.getGroup()+"&"+ path + "&" +interfaceConfig.getVersion()+ "&" +"providers";

        // 判断zk/redis。创建zk连接，并创建RegistryDirectory，第一次时创建DubboInvoker
        registryService = RegistryManager.getRegistryService(registryAddress);
        registryService.subscribe(providerPath, this);
    }

    /**
     * 注意，这里需要try/catch，否则抛异常后不会再有通知
     * @param path
     * @param children
     */
    @Override
    public synchronized void childChanged(String path, List<String> children) {
        log.info("监听到zk路径变化:{}，children:{}", path, children);
        try {
            if (children == null || children.size() == 0) {
                // 可能是远程抖动，或者zookeeper出问题了，造成所有服务实例下线，这里还需要通过心跳检测。
                log.info("监听到zk路径无子节点:{}", providerPath);
                children = Lists.newArrayList();
            }
            List<String> added = children.stream()
                    .filter(one -> !ipAndPortAndWeight2InvokerMap.containsKey(one))
                    .collect(Collectors.toList());
            List<String> finalChildren = children;
            List<String> deleted = ipAndPortAndWeight2InvokerMap.keySet().stream()
                    .filter(one -> !finalChildren.contains(one))
                    .collect(Collectors.toList());
            log.info("监听到zk路径：{}，子节点变化，新增zk节点：{}，删除zk节点：{}", providerPath, added, deleted);

            added.forEach(ipAndPortAndWeight -> {
                if (!ipAndPortAndWeight2InvokerMap.containsKey(ipAndPortAndWeight)) {
                    ipAndPortAndWeight2InvokerMap.put(ipAndPortAndWeight, new WheelInvoker(ipAndPortAndWeight,interfaceConfig));
                }
            });
            deleted.forEach(ipAndPortAndWeight -> {
                // 运行时删除
                ipAndPortAndWeight2InvokerMap.get(ipAndPortAndWeight).destroy();
                ipAndPortAndWeight2InvokerMap.remove(ipAndPortAndWeight);
            });
        } catch (Exception e) {
            log.error("处理zk事件出错", e);
        }
    }

    public List<WheelInvoker> getInvokerList() {
        // 找出可用的，zk有子节点，不表示节点底层网络可连通，例如zk超时时间比较长，还没有检测到
        return ipAndPortAndWeight2InvokerMap.values().stream().filter(dubboInvoker -> {
            boolean available = dubboInvoker.isAvailable();
            if (!available) {
                log.warn("zk上该节点存在，但是底层网络探测到已经断开：{}", dubboInvoker.getIpAndPort());
            }
            return available;
        }).collect(Collectors.toList());
    }

    /**
     * 销毁
     */
    public void destroy() {
        // 先解除订阅，再销毁invoker
        registryService.unsubscribe(providerPath);
        // 减少引用，引用为0再关闭
        RegistryManager.remove(registryAddress);

        ipAndPortAndWeight2InvokerMap.forEach((key, dubboInvoker) -> {
            dubboInvoker.destroy();
        });
        ipAndPortAndWeight2InvokerMap.clear();
    }
}
