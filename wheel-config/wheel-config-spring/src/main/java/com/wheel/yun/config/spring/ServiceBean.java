package com.wheel.yun.config.spring;



import com.wheel.yun.common.config.InterfaceConfig;
import com.wheel.yun.common.exporter.WheelExporter;
import com.wheel.yun.config.common.ApplicationConfig;
import com.wheel.yun.config.common.ProtocolConfig;
import com.wheel.yun.config.common.RegistryConfig;
import com.wheel.yun.config.common.ServiceConfig;
import com.wheel.yun.config.spring.util.NetUtils;
import com.wheel.yun.config.spring.util.WheelBeanUtils;
import com.wheel.yun.registry.api.RegistryManager;
import com.wheel.yun.registry.comm.RegistryService;
import com.wheel.yun.remote.netty.NettyManager;
import com.wheel.yun.remote.netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/5/29 14:35
 */
@Slf4j
public class ServiceBean<T> extends ServiceConfig<T> implements SmartInitializingSingleton, DisposableBean,
        ApplicationContextAware, BeanNameAware {


    private AtomicBoolean exported = new AtomicBoolean();

    private AtomicBoolean unexported = new AtomicBoolean();

    private transient ApplicationContext applicationContext;


    private transient String beanName;

    private NettyServer nettyServer;
    private List<String> providerPath;

    private String ipAndPort;

    private Integer nettyPort;

    private List<InterfaceConfig> interfaceConfigs;

    private List<String> clazzNames;
    private List<Object> ref_replaces;

    /**
     * zk地址
     */
    private RegistryService registryService;




    @Override
    public void afterSingletonsInstantiated() {
        export();
    }
    @Override
    public void setBeanName(String s) {
        this.beanName = s;
    }

    @Override
    public void destroy() throws Exception {
        unexport();
    }

    public void export(){
        if (!exported.compareAndSet(false, true)) {
            return;
        }

        // 得到xml文件配置的serviceBean
        List<ServiceBean> serviceBean = WheelBeanUtils.getServiceBean(applicationContext, ServiceBean.class);

        this.clazzNames = new ArrayList<>();
        for(int i = 0;i<serviceBean.size();i++){
            clazzNames.add(serviceBean.get(i).getInterface());
        }
        List<InterfaceConfig> interfaceConfig = transForm(serviceBean);
        this.interfaceConfigs = interfaceConfig;
        List<ProtocolConfig> protocolConfig = WheelBeanUtils.getProtocolConfig(applicationContext, ProtocolConfig.class);

        List<RegistryConfig> registryConfig = WheelBeanUtils.getRegistryConfig(applicationContext, RegistryConfig.class);


        // 先默认定为用户配置的第一个配置有效
        this.ipAndPort = registryConfig.get(0).getHost()+":"+registryConfig.get(0).getPort();

        ApplicationConfig applicationConfig = WheelBeanUtils.getApplicationConfig(applicationContext, ApplicationConfig.class);

        this.ref_replaces = WheelBeanUtils.getRef(applicationContext,serviceBean);
        WheelExporter.exportService(this.clazzNames, interfaceConfig,ref_replaces);
        this.nettyPort = protocolConfig.get(0).getPort();
        if("dubbo".equals(protocolConfig.get(0).getProtocol())){
            nettyServer = NettyManager.getNettyServer(nettyPort);
        }else{
            throw new RuntimeException("unknown communicate protocol:" + protocolConfig.get(0).getProtocol());
        }

        // 判断什么类型的注册中心
        registryService = RegistryManager.getRegistryService(this.ipAndPort);
        setProviderPath(serviceBean);

        this.registry();

    }


    private void registry(){
        for(int i = 0;i<providerPath.size();i++){
            registryService.register(providerPath.get(i));
        }
    }
    private void setProviderPath(List<ServiceBean> serviceBeans){
        providerPath = new ArrayList<>();
        for(int i = 0;i<serviceBeans.size();i++){
            String group = interfaceConfigs.get(i).getGroup();
            String version =  interfaceConfigs.get(i).getVersion();
            providerPath.add("/wheel/"+group+"&"+clazzNames.get(i)+"&"+version+"&"+"providers"+"/"+ NetUtils.getServerIp() + ":"
                    +nettyPort+"@"+serviceBeans.get(i).getLoadbalance()+"_"+serviceBeans.get(i).getWeight());
        }
    }
    public List<InterfaceConfig> transForm(List<ServiceBean> serviceBean){
        List<InterfaceConfig> interfaceConfigs = new ArrayList<>();
        for(int i = 0;i<serviceBean.size();i++){
            InterfaceConfig interfaceConfig = new InterfaceConfig();
            String group = serviceBean.get(i).getGroup();
            if(group == null || group.length() == 0)
                group = "DEFAULT_GROUP";
            interfaceConfig.setGroup(group);
            interfaceConfig.setFailStrategy(serviceBean.get(i).getFailStrategy());
            interfaceConfig.setRetryCount(serviceBean.get(i).getRetryCount());
            interfaceConfig.setTimeout(serviceBean.get(i).getTimeout());
            String version =  interfaceConfig.getVersion();
            if(version == null || version.length() == 0)
                version = "1.0.0";
            interfaceConfig.setVersion(version);
            interfaceConfigs.add(interfaceConfig);
        }

        return interfaceConfigs;
    }


    /**
     * 逆向操作
     * 引用计数销毁 统统使用RegistryManager.getRegistryService管理
     * @throws Exception
     */
    public void unexport() throws Exception {
        if (!exported.get() || !unexported.compareAndSet(false, true)) {
            return;
        }

        // 需要unregister
        this.unregister();
        // 减少引用，引用为0再关闭。这两个其实可以不移除，一般占用不会太多，client可能太多，需要移除
        RegistryManager.remove(this.ipAndPort);
        NettyManager.removeNettyServer(nettyPort);
        WheelExporter.remove(clazzNames, interfaceConfigs);
    }
    private void unregister(){
        for(int i = 0;i<providerPath.size();i++){
            registryService.unregister(providerPath.get(i));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
