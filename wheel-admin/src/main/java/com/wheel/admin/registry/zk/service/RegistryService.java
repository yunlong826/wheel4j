package com.wheel.admin.registry.zk.service;

import java.io.Closeable;

/**
 * @author jack_yun
 * @version 1.0
 * @description: 注册中心接口
 * @date 2022/7/30 14:44
 */
public interface RegistryService extends Closeable {

    /**
     * 订阅prividerPath路径下的子节点变化，有变化时通过listener通知我
     * @param providerPath "/wheel/"+interfaceConfigs.get(i).getGroup()+"/"+clazzNames.get(i)+"/providers"+"/"+ NetUtils.getServerIp() + ":"
     *                     +nettyPort+"@"+serviceBeans.get(i).getLoadbalance()+"_"+serviceBeans.get(i).getWeight()
     * @param childListener
     */
    void subscribe(String providerPath, ChildListener childListener);


}
