package com.wheel.yun.config.spring.util;


import com.wheel.yun.config.common.ApplicationConfig;
import com.wheel.yun.config.common.ProtocolConfig;
import com.wheel.yun.config.common.RegistryConfig;
import com.wheel.yun.config.spring.ReferenceBean;
import com.wheel.yun.config.spring.ServiceBean;
import org.springframework.beans.factory.BeanFactory;

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


    public static ServiceBean getServiceBean(BeanFactory beanFactory, Class<?> cls){
        String beanName = cls.getName();
        if(beanFactory.containsBean(beanName)){
            return (ServiceBean) beanFactory.getBean(beanName,cls);
        }
        return null;
    }
    public static Object getRef(BeanFactory beanFactory,String ref_name){
        if(beanFactory.containsBean(ref_name)){
            return beanFactory.getBean(ref_name);
        }
        return null;
    }

    public static ProtocolConfig getProtocolConfig(BeanFactory beanFactory, Class<?> cls){
        String beanName = cls.getName();
        if(beanFactory.containsBean(beanName)){
            return (ProtocolConfig) beanFactory.getBean(beanName,cls);
        }
        return null;
    }

    public static RegistryConfig getRegistryConfig(BeanFactory beanFactory, Class<?> cls){
        String beanName = cls.getName();
        if(beanFactory.containsBean(beanName)){
            RegistryConfig bean = (RegistryConfig)beanFactory.getBean(beanName, cls);
            return bean;
        }
        return null;
    }
    public static ApplicationConfig getApplicationConfig(BeanFactory beanFactory, Class<?> cls){
        String beanName = cls.getName();
        if(beanFactory.containsBean(beanName)){
            return (ApplicationConfig) beanFactory.getBean(beanName,cls);
        }
        return null;
    }
}
