package com.wheel.yun.common.exporter;



import com.wheel.yun.common.config.InterfaceConfig;
import com.wheel.yun.common.invoker.RpcInvocation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/29 18:59
 */
public class WheelExporter {
    private static Map<String, Object> exportServiceMap = new ConcurrentHashMap();


    public static void exportService(List<String> clazzName, List<InterfaceConfig> interfaceConfigs, List<Object> refs){
        // 三个数组的长度都是一样的
        for(int i = 0;i<clazzName.size();i++){
            exportService(clazzName.get(i),interfaceConfigs.get(i),refs.get(i));
        }
    }
    private static void exportService(String clazzName, InterfaceConfig interfaceConfig, Object ref) {
        String key = buildKey(clazzName, interfaceConfig.getVersion());
        exportServiceMap.put(key, ref);
    }

    public static void remove(List<String> clazzName,List<InterfaceConfig> interfaceConfigs){
        for(int i = 0;i<clazzName.size();i++){
            remove(clazzName.get(i),interfaceConfigs.get(i));
        }
    }
    private static void remove(String clazzName, InterfaceConfig interfaceConfig) {
        String key = buildKey(clazzName, interfaceConfig.getVersion());
        exportServiceMap.remove(key);
    }

    public static Object getService(RpcInvocation rpcInvocation) {
        String key = buildKey(rpcInvocation.getInterfaceName(), rpcInvocation.getVersion());
        return exportServiceMap.get(key);
    }

    private static String buildKey(String clazzName, String version) {
        return String.format("%s_%s", clazzName, version);
    }
}
