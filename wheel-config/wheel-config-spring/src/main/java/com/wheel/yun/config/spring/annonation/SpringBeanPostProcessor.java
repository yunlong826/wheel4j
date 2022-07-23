package com.wheel.yun.config.spring.annonation;

import com.wheel.yun.common.config.InterfaceConfig;
import com.wheel.yun.common.exporter.WheelExporter;
import com.wheel.yun.config.common.ProtocolConfig;
import com.wheel.yun.config.common.ReferenceProxy;
import com.wheel.yun.config.common.RegistryConfig;
import com.wheel.yun.config.spring.ServiceBean;
import com.wheel.yun.config.spring.util.NetUtils;
import com.wheel.yun.config.spring.util.PropertiesFileUtil;
import com.wheel.yun.registry.api.RegistryManager;
import com.wheel.yun.registry.comm.RegistryService;
import com.wheel.yun.remote.netty.NettyManager;
import com.wheel.yun.remote.netty.server.NettyServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/16 15:19
 */
@Slf4j
@Component
public class SpringBeanPostProcessor implements BeanFactoryPostProcessor, EnvironmentAware {

    private final List<ServiceBean> serviceBeans = new ArrayList<>();

    private final List<String> clazzNames = new ArrayList<>();

    private final List<InterfaceConfig> interfaceConfigs = new ArrayList<>();

    private final List<ProtocolConfig> protocolConfigs = new ArrayList<>();

    private final List<RegistryConfig> registryConfigs = new ArrayList<>();

    private Environment environment;

    private String ipAndPort;

    private final List<Object> ref_replaces = new ArrayList<>();

    private Integer nettyPort;

    private NettyServer nettyServer;

    // 该值可以配置多个地址,英文逗号分割
    // tg: zookeeper://127.0.0.1:2181,zookeeper://127.1.1.2:2181 ...
    private String registryAddress;

    // 该值可以配置多个地址,英文逗号分割
    // tg: dubbo:8080,wheel:8081 ...
    private String protocol;



    /**
     * zk地址
     */
    private RegistryService registryService;

    private final List<String> providerPaths = new ArrayList<>();


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        Properties properties = PropertiesFileUtil.readPropertiesFile("application.properties");

        setRegistryAddress(properties.getProperty("wheel.registry.address"));

        setProtocol(properties.getProperty("wheel.protocol"));
        setConfigs(beanFactory,ProtocolConfig.class,protocolConfigs);

        setConfigs(beanFactory,RegistryConfig.class,registryConfigs);

        // 当服务端启动的时候，主要是执行该方法
        preServiceBeanPostProcess(beanFactory);

        // 当消费端启动的时候，主要是执行该方法
        postReferenceBeanPostProcess(beanFactory);


    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    private void postReferenceBeanPostProcess(ConfigurableListableBeanFactory beanFactory) {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for(String beanName:beanDefinitionNames){
            Object bean = beanFactory.getBean(beanName);
            Class<?> clazz = bean.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for(Field field:declaredFields){
                boolean isExist = field.isAnnotationPresent(Reference.class);
                if(isExist){
                    try{
                        if (!field.getType().isInterface()) {
                            throw new RuntimeException("wheel依赖不是接口：" + field.getType().getName());
                        }
                        Reference ref = field.getAnnotation(Reference.class);
                        log.info("尝试注入接口代理，bean:{} 属性为：{}", clazz.getName(), field.getName());
                        field.setAccessible(true);
                        InterfaceConfig interfaceConfig = transformAnno(ref);
                        log.info("registryConfig的地址:{}",registryConfigs.get(0).getHost());
                        ReferenceProxy referenceProxy = new ReferenceProxy(field.getType(),interfaceConfig,registryConfigs.get(0));
                        log.info("代理对象：{}",referenceProxy.getProxy());
                        field.set(bean,referenceProxy.getProxy());
                    }catch (IllegalAccessException e){
                        log.error("设置jdk实例出错啦：{}", field);
                    }
                }
            }
        }
    }

    private InterfaceConfig transformAnno(Reference ref) {
        InterfaceConfig interfaceConfig = new InterfaceConfig();
        interfaceConfig.setGroup(ref.group());
        interfaceConfig.setVersion(ref.version());
        interfaceConfig.setTimeout(ref.timeout());
        interfaceConfig.setFailStrategy(ref.failStrategy());
        interfaceConfig.setRetryCount(ref.retryCount());
        return interfaceConfig;
    }

    private void registry(){
        for(int i = 0;i<providerPaths.size();i++){
            registryService.register(providerPaths.get(i));
        }
    }
    private void setProviderPath(List<ServiceBean> serviceBeans){
        for(int i = 0;i<serviceBeans.size();i++){
            providerPaths.add("/wheel/"+interfaceConfigs.get(i).getGroup()+clazzNames.get(i)+"/providers"+"/"+ NetUtils.getServerIp() + ":"
                    +nettyPort+"@"+serviceBeans.get(i).getLoadbalance()+"_"+serviceBeans.get(i).getWeight());
        }
    }

    private void setRefRepalces(ConfigurableListableBeanFactory beanFactory) {
        for(int i = 0;i<serviceBeans.size();i++){
            ref_replaces.add(beanFactory.getBean(serviceBeans.get(i).getRef()));
        }
    }

    private <T> void setConfigs(ConfigurableListableBeanFactory beanFactory,Class<T> cls,List<T> lists) {
        String[] beanNamesForType = beanFactory.getBeanNamesForType(cls);
        for(String beanName:beanNamesForType){
            lists.add((T) beanFactory.getBean(beanName));
        }
        if(cls.equals(RegistryConfig.class)){
            // tg: zookeeper://127.0.0.1:2181,zookeeper://127.1.1.2:2181 ...
            String[] split = StringUtils.split(registryAddress, ",");
            if(split == null || split.length == 0)
                return;
            for(String address:split){
                RegistryConfig registryConfig = new RegistryConfig(address);
                lists.add((T) registryConfig);
                beanFactory.registerSingleton(registryConfig.getClass().getName(),registryConfig);
            }
        }else{
            // tg: dubbo:8080,wheel:8081 ...
            String[] split = StringUtils.split(protocol, ",");
            if(split == null || split.length == 0)
                return;
            for(String p:split){
                int idx = p.indexOf(":");
                String agree = p.substring(0,idx);
                String port = p.substring(idx+1);
                ProtocolConfig protocolConfig = new ProtocolConfig();
                protocolConfig.setProtocol(agree);
                protocolConfig.setProtocol(port);
                lists.add((T) protocolConfig);
                beanFactory.registerSingleton(protocolConfig.getClass().getName(),protocolConfig);
            }
        }
    }

    private void preServiceBeanPostProcess(ConfigurableListableBeanFactory beanFactory) {
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for(String beanName:beanDefinitionNames){
            Object bean = beanFactory.getBean(beanName);
            if(bean.getClass().isAnnotationPresent(WheelService.class)){
                // 得到WheelService注解
                WheelService annotation = bean.getClass().getAnnotation(WheelService.class);

                ServiceBean serviceBean = new ServiceBean();
                serviceBean.setGroup(annotation.group());
                serviceBean.setFailStrategy(annotation.failStrategy());
                serviceBean.setInterface(annotation.interfaceClass().getName());
                serviceBean.setLoadbalance(annotation.loadBalance());
                serviceBean.setRef(beanName);
                serviceBean.setRetryCount(annotation.retryCount());
                serviceBean.setTimeout(annotation.timeout());
                serviceBean.setVersion(annotation.version());
                serviceBean.setWeight(Integer.valueOf(annotation.weight()));
                serviceBeans.add(serviceBean);
            }
        }
        if(serviceBeans.size() == 0)
            return;
        setClazzNames();

        setInterfaceConfigs();


        // 先默认为第一个，后续扩展时再改
        this.ipAndPort = registryConfigs.get(0).getHost()+":"+registryConfigs.get(0).getPort();

        setRefRepalces(beanFactory);

        this.nettyPort = protocolConfigs.get(0).getPort();

        // 暴露服务
        WheelExporter.exportService(this.clazzNames, this.interfaceConfigs,this.ref_replaces);

        if("dubbo".equals(protocolConfigs.get(0).getProtocol())){
            nettyServer = NettyManager.getNettyServer(nettyPort);
        }else{
            throw new RuntimeException("unknown communicate protocol:" + protocolConfigs.get(0).getProtocol());
        }

        // 判断什么类型的注册中心
        registryService = RegistryManager.getRegistryService(this.ipAndPort);
        setProviderPath(serviceBeans);

        this.registry();
    }
    private void setClazzNames(){
        for(int i = 0;i<serviceBeans.size();i++){
            clazzNames.add(serviceBeans.get(i).getInterface());
        }
    }

    private void setInterfaceConfigs(){
        for(int i = 0;i<serviceBeans.size();i++){
            InterfaceConfig interfaceConfig = new InterfaceConfig();
            interfaceConfig.setGroup(serviceBeans.get(i).getGroup());
            interfaceConfig.setFailStrategy(serviceBeans.get(i).getFailStrategy());
            interfaceConfig.setRetryCount(serviceBeans.get(i).getRetryCount());
            interfaceConfig.setTimeout(serviceBeans.get(i).getTimeout());
            interfaceConfig.setVersion(serviceBeans.get(i).getVersion());
            interfaceConfigs.add(interfaceConfig);
        }

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    public Environment getEnvironment(){
        return this.environment;
    }
}
