package com.wheel.yun.config.spring;

import com.google.common.collect.Maps;
import com.wheel.yun.common.config.InterfaceConfig;
import com.wheel.yun.config.common.ReferenceConfig;
import com.wheel.yun.config.common.RegistryConfig;
import com.wheel.yun.config.common.cache.WheelBeanDefinitionCache;
import com.wheel.yun.config.spring.annonation.Reference;
import com.wheel.yun.config.spring.util.WheelBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/30 16:13
 */
@Slf4j
public class ReferenceBean<T> extends ReferenceConfig<T> implements BeanFactoryPostProcessor,ApplicationContextAware {

    private ApplicationContext applicationContext;
    private RegistryConfig registryConfig;
    private Map<String,ReferenceConfig> referenceConfigMap = Maps.newHashMap();
    private BeanDefinitionRegistry beanDefinitionRegistry;

    public ReferenceBean(){
        super();
    }



    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.registryConfig = WheelBeanUtils.getRegistryConfig(beanFactory,RegistryConfig.class)[0];

        List<String> list = WheelBeanDefinitionCache.getList(ReferenceBean.class);
        for(int i = 0;i<list.size();i++){
            ReferenceBean bean = (ReferenceBean) beanFactory.getBean(list.get(i));
            InterfaceConfig interfaceConfig = transform(bean);

            // 需要被代理的接口
            Class<?> cls = ClassUtils.resolveClassName(bean.getInterface(), this.getClass().getClassLoader());
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(cls);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
            definition.getPropertyValues().add("interfaceClass", definition.getBeanClassName());
            definition.getPropertyValues().add("registryConfig",registryConfig);
            definition.getPropertyValues().add("interfaceConfig",interfaceConfig);
            definition.setBeanClass(ProxyFactory.class);
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            // 注册bean名,一般为类名首字母小写
            beanDefinitionRegistry.registerBeanDefinition(bean.getId(), definition);

//            ReferenceBean bean = (ReferenceBean) beanFactory.getBean(list.get(i));
//            RootBeanDefinition beanDefinition = new RootBeanDefinition();
//            Class<?> clazz = ClassUtils.resolveClassName(bean.getInterface(), this.getClass().getClassLoader());
//            InterfaceConfig interfaceConfig = transform(bean);
//            ReferenceProxy referenceProxy = new ReferenceProxy(clazz,interfaceConfig,registryConfig);
//            log.info("代理对象：{}",referenceProxy.getProxy());
//            beanDefinition.setBeanClassName(referenceProxy.getProxy().getClass().getName());
//            beanDefinition.setBeanClass(referenceProxy.getProxy().getClass());
//            beanDefinitionRegistry.registerBeanDefinition(bean.getId(),beanDefinition);


        }
//        for(String beanDefinitionName:beanFactory.getBeanDefinitionNames()){
//            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
//            String beanClassName = beanDefinition.getBeanClassName();
//            if(beanClassName != null){
//                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, this.getClass().getClassLoader());
//                Object bean = applicationContext.getBean(clazz);
//                Field[] declaredFields = clazz.getDeclaredFields();
//                for(Field field:declaredFields){
//                    boolean isExist = field.isAnnotationPresent(Reference.class);
//                    if(isExist){
//                        try{
//                            if (!field.getType().isInterface()) {
//                                throw new RuntimeException("wheel依赖不是接口：" + field.getType().getName());
//                            }
//                            Reference ref = field.getAnnotation(Reference.class);
//                            log.info("尝试注入接口代理，bean:{} 属性为：{}", clazz.getName(), field.getName());
//                            field.setAccessible(true);
//                            InterfaceConfig interfaceConfig = transform(ref);
//                            log.info("registryConfig的地址:{}",registryConfig.getHost());
//                            ReferenceProxy referenceProxy = new ReferenceProxy(field.getType(),interfaceConfig,registryConfig);
//                            log.info("代理对象：{}",referenceProxy.getProxy());
//                            field.set(bean,referenceProxy.getProxy());
//                        }catch (IllegalAccessException e){
//                            log.error("设置jdk实例出错啦：{}", field);
//                        }
//                    }
//                }
//            }
//        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
    }
    private InterfaceConfig transform(Reference ref) {
        InterfaceConfig interfaceConfig = new InterfaceConfig();
        interfaceConfig.setGroup(ref.group());
        interfaceConfig.setVersion(ref.version());
        interfaceConfig.setTimeout(ref.timeout());
        interfaceConfig.setFailStrategy(ref.failStrategy());
        interfaceConfig.setRetryCount(ref.retryCount());
        return interfaceConfig;
    }
    private InterfaceConfig transform(ReferenceBean ref) {
        InterfaceConfig interfaceConfig = new InterfaceConfig();
        interfaceConfig.setGroup(ref.getGroup());
        interfaceConfig.setVersion(ref.getVersion());
        interfaceConfig.setTimeout(ref.getTimeout());
        interfaceConfig.setFailStrategy(ref.getFailStrategy());
        interfaceConfig.setRetryCount(ref.getRetryCount());
        return interfaceConfig;
    }
}
