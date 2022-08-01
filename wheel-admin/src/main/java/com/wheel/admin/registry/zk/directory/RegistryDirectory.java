package com.wheel.admin.registry.zk.directory;

import com.google.common.collect.Lists;
import com.wheel.admin.registry.zk.manager.RegistryManager;
import com.wheel.admin.registry.zk.service.ChildListener;
import com.wheel.admin.registry.zk.service.RegistryService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/30 15:14
 */
@Slf4j
public class RegistryDirectory implements ChildListener {

    private final String providerPath = "/wheel";

    private String registryAddress;

    private RegistryService registryService;


    public RegistryDirectory(String registryAddress){
        this.registryAddress = registryAddress;
        registryService = RegistryManager.getRegistryService(registryAddress);
        registryService.subscribe(providerPath,this);
    }

    @Override
    public synchronized void childChanged(String path, List<String> children) {
        log.info("监听到zk路径变化:{}，children:{}", path, children);
        try {
            if (children == null || children.size() == 0) {
                // 可能是远程抖动，或者zookeeper出问题了，造成所有服务实例下线，这里还需要通过心跳检测。
                log.info("监听到zk路径无子节点:{}", providerPath);
            }
        } catch (Exception e) {
            log.error("处理zk事件出错", e);
        }
    }
}
