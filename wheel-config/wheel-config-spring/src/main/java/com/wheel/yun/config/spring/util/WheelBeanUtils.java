package com.wheel.yun.config.spring.util;


import com.wheel.yun.config.common.ApplicationConfig;
import com.wheel.yun.config.common.ProtocolConfig;
import com.wheel.yun.config.common.RegistryConfig;
import com.wheel.yun.config.common.cache.WheelBeanDefinitionCache;
import com.wheel.yun.config.spring.ReferenceBean;
import com.wheel.yun.config.spring.ServiceBean;
import org.springframework.beans.factory.BeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/30 17:01
 */
public class WheelBeanUtils {

    public static String ADDRESS = "";

    public static Object getRpcApi(BeanFactory beanFactory, String s){
        if(beanFactory.containsBean(s)){
            return beanFactory.getBean(s);
        }
        return null;
    }

    public static ReferenceBean getReferenceBean(BeanFactory beanFactory, Class<?> cls){
        String beanName = cls.getName();
        if(beanFactory.containsBean(beanName)){
            return (ReferenceBean) beanFactory.getBean(beanName,cls);
        }
        return null;
    }


    public static List<ServiceBean> getServiceBean(BeanFactory beanFactory, Class<?> cls){
        List<String> list = WheelBeanDefinitionCache.getList(cls);
        List<ServiceBean> pcs = new ArrayList<>();
        if(list == null || list.size() == 0)
            return pcs;

        for(int i = 0;i<list.size();i++){
            pcs.add((ServiceBean) beanFactory.getBean(list.get(i)));
        }
        return pcs;
    }
    public static List<Object> getRef(BeanFactory beanFactory,List<ServiceBean> serviceBeans){
        List<Object> refs = new ArrayList<>();
        if(serviceBeans == null || serviceBeans.size() == 0)
                return refs;
        for(int i = 0;i<serviceBeans.size();i++){
            refs.add(beanFactory.getBean(serviceBeans.get(i).getRef()));
        }
        return refs;
    }

    public static List<ProtocolConfig> getProtocolConfig(BeanFactory beanFactory, Class<?> cls){
        List<String> list = WheelBeanDefinitionCache.getList(cls);
        List<ProtocolConfig> pcs = new ArrayList<>();

        if(list == null || list.size() == 0){
            return pcs;
        }
        for(int i = 0;i<list.size();i++){
            pcs.add((ProtocolConfig) beanFactory.getBean(list.get(i)));
        }
        return pcs;
    }

    public static List<RegistryConfig> getRegistryConfig(BeanFactory beanFactory, Class<?> cls){
        List<String> list = WheelBeanDefinitionCache.getList(cls);
        List<RegistryConfig> rcs = new ArrayList<>();

        if(list == null || list.size() == 0){
            return rcs;
        }

        for(int i = 0;i<list.size();i++){
            rcs.add((RegistryConfig) beanFactory.getBean(list.get(i)));
        }
        return rcs;
    }
    public static ApplicationConfig getApplicationConfig(BeanFactory beanFactory, Class<?> cls){
        String beanName = cls.getName();
        if(beanFactory.containsBean(beanName)){
            return (ApplicationConfig) beanFactory.getBean(beanName);
        }
        return null;
    }
}
