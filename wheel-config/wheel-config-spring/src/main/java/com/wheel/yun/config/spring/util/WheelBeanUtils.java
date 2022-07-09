package com.wheel.yun.config.spring.util;


import com.wheel.yun.config.common.ApplicationConfig;
import com.wheel.yun.config.common.ProtocolConfig;
import com.wheel.yun.config.common.RegistryConfig;
import com.wheel.yun.config.common.cache.WheelBeanDefinitionCache;
import com.wheel.yun.config.spring.ReferenceBean;
import com.wheel.yun.config.spring.ServiceBean;
import org.springframework.beans.factory.BeanFactory;

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


    public static ServiceBean[] getServiceBean(BeanFactory beanFactory, Class<?> cls){
        List<String> list = WheelBeanDefinitionCache.getList(cls);
        ServiceBean[] pcs = new ServiceBean[list.size()];

        for(int i = 0;i<list.size();i++){
            pcs[i] = (ServiceBean) beanFactory.getBean(list.get(i));
        }
        return pcs;
    }
    public static Object[] getRef(BeanFactory beanFactory,ServiceBean[] serviceBeans){
        Object[] refs = new Object[serviceBeans.length];
        for(int i = 0;i<serviceBeans.length;i++){
            refs[i] = beanFactory.getBean(serviceBeans[i].getRef());
        }
        return refs;
    }

    public static ProtocolConfig[] getProtocolConfig(BeanFactory beanFactory, Class<?> cls){
        List<String> list = WheelBeanDefinitionCache.getList(cls);
        ProtocolConfig[] pcs = new ProtocolConfig[list.size()];

        for(int i = 0;i<list.size();i++){
            pcs[i] = (ProtocolConfig) beanFactory.getBean(list.get(i));
        }
        return pcs;
    }

    public static RegistryConfig[] getRegistryConfig(BeanFactory beanFactory, Class<?> cls){
        List<String> list = WheelBeanDefinitionCache.getList(cls);
        RegistryConfig[] rcs = new RegistryConfig[list.size()];

        for(int i = 0;i<list.size();i++){
            rcs[i] = (RegistryConfig) beanFactory.getBean(list.get(i));
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
