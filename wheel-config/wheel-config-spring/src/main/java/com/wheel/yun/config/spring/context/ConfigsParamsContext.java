package com.wheel.yun.config.spring.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 自定义xml配置中的键值对参数值
 *  tg: <xsd:attribute name="id" type="xsd:string"></xsd:attribute>
 *      <xsd:attribute name="address" type="xsd:string"></xsd:attribute>
 *
 * @author longyun
 * @version 1.0
 * @date 2022/8/3 10:31
 */
public class ConfigsParamsContext {
    public static Map<Class, List<String>> PARAMS = new HashMap<>();

    public static Map<Class,List<String>> ClassMapBeanName = new HashMap<>();

    public static void putParams(Class clazz,List<String> params){
        PARAMS.put(clazz,params);
    }

    public static void putClassAndBeanName(Class clazz,List<String> ids){
        ClassMapBeanName.put(clazz,ids);
    }
}
