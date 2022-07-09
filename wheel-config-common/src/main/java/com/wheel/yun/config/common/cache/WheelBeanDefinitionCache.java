package com.wheel.yun.config.common.cache;





import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jack_yun
 * @version 1.0
 * @description: 缓存BeanDefinition的相关唯一标识，比如xml定义的id等信息，以提供为ServiceBean使用
 * @date 2022/7/9 22:51
 */
public abstract class WheelBeanDefinitionCache {

    private static Map<Class<?>, List<String>> CacheMap = new HashMap<>();



    public static void putCache(Class<?> cls,String s){
        List<String> orDefault = CacheMap.getOrDefault(cls, new ArrayList<>());
        orDefault.add(s);
        CacheMap.put(cls,orDefault);
    }

    public static List<String> getList(Class<?> cls){
        return CacheMap.get(cls);
    }



}
