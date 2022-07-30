package com.wheel.admin.registry.zk.service;

import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

/**
 * @author jack_yun
 * @version 1.0
 * @description: 节点数据监听
 * @date 2022/7/30 14:56
 */
public interface DataListener {
    void dataChanged(String path, Object value, TreeCacheEvent.Type eventType);
}
