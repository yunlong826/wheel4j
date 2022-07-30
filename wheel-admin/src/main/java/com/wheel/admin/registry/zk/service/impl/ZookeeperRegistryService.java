package com.wheel.admin.registry.zk.service.impl;

import com.wheel.admin.registry.zk.CuratorZookeeperClient;
import com.wheel.admin.registry.zk.service.ChildListener;
import com.wheel.admin.registry.zk.service.RegistryService;

import java.io.IOException;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/30 14:50
 */
public class ZookeeperRegistryService implements RegistryService {

    private CuratorZookeeperClient curatorZookeeperClient;

    public ZookeeperRegistryService(String registryAddress) {
        curatorZookeeperClient = new CuratorZookeeperClient(registryAddress);
    }


    @Override
    public void subscribe(String providerPath, ChildListener childListener) {
        // 添加watcher
        curatorZookeeperClient.addTargetChildListener(providerPath, childListener);
    }


    @Override
    public void close() throws IOException {
        curatorZookeeperClient.doClose();
    }
}
