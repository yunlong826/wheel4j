package com.wheel.admin.registry.zk.service;

import java.util.List;

/**
 * @author jack_yun
 * @version 1.0
 * @description: 监听子节点的变化
 * @date 2022/7/30 14:48
 */
public interface ChildListener {
    void childChanged(String path, List<String> children);
}
