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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/29 14:35
 */
public class ServiceBean<T> extends ServiceConfig<T> implements SmartInitializingSingleton, DisposableBean,
        ApplicationContextAware, BeanNameAware {

//    private final transient Wheel wheel;

    private AtomicBoolean exported = new AtomicBoolean();

    private AtomicBoolean unexported = new AtomicBoolean();

    private transient ApplicationContext applicationContext;


    private transient String beanName;

    private NettyServer nettyServer;
    private String[] providerPath;

    private String ipAndPort;

    private Integer nettyPort;

    private InterfaceConfig[] interfaceConfigs;

    private String[] clazzNames;
    private Object[] ref_replaces;

    /**
     * zk地址
     */
    private RegistryService registryService;


//    public ServiceBean(){
//        this.wheel = null;
//    }

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

        ServiceBean[] serviceBean = WheelBeanUtils.getServiceBean(applicationContext, ServiceBean.class);
        this.clazzNames = new String[serviceBean.length];
        for(int i = 0;i<serviceBean.length;i++){
            clazzNames[i] = serviceBean[i].getInterface();
        }
        InterfaceConfig[] interfaceConfig = transForm(serviceBean);
        this.interfaceConfigs = interfaceConfig;
        ProtocolConfig[] protocolConfig = WheelBeanUtils.getProtocolConfig(applicationContext, ProtocolConfig.class);
        RegistryConfig[] registryConfig = WheelBeanUtils.getRegistryConfig(applicationContext, RegistryConfig.class);
        // 先默认定为用户配置的第一个配置有效
        this.ipAndPort = registryConfig[0].getHost()+":"+registryConfig[0].getPort();
        ApplicationConfig applicationConfig = WheelBeanUtils.getApplicationConfig(applicationContext, ApplicationConfig.class);

        this.ref_replaces = WheelBeanUtils.getRef(applicationContext,serviceBean);
        WheelExporter.exportService(this.clazzNames, interfaceConfig,ref_replaces);
        this.nettyPort = protocolConfig[0].getPort();
        if("dubbo".equals(protocolConfig[0].getProtocol())){
            nettyServer = NettyManager.getNettyServer(nettyPort);
        }else{
            throw new RuntimeException("unknown communicate protocol:" + protocolConfig[0].getProtocol());
        }
        // 判断什么类型的注册中心
        registryService = RegistryManager.getRegistryService(this.ipAndPort);
        setProviderPath(serviceBean);

        this.registry();

    }

    private void registry(){
        for(int i = 0;i<providerPath.length;i++){
            registryService.register(providerPath[i]);
        }
    }
    private void setProviderPath(ServiceBean[] serviceBeans){
        providerPath = new String[serviceBeans.length];
        for(int i = 0;i<serviceBeans.length;i++){
            providerPath[i] = "/wheel/"+interfaceConfigs[i].getGroup()+clazzNames[i]+"/providers"+"/"+ NetUtils.getServerIp() + ":"
                    +nettyPort+"@"+serviceBeans[i].getLoadbalance()+"_"+serviceBeans[i].getWeight();
        }
    }
    public InterfaceConfig[] transForm(ServiceBean[] serviceBean){
        InterfaceConfig[] interfaceConfigs = new InterfaceConfig[serviceBean.length];
        for(int i = 0;i<serviceBean.length;i++){
            interfaceConfigs[i] = new InterfaceConfig();
            interfaceConfigs[i].setGroup(serviceBean[i].getGroup());
            interfaceConfigs[i].setFailStrategy(serviceBean[i].getFailStrategy());
            interfaceConfigs[i].setRetryCount(serviceBean[i].getRetryCount());
            interfaceConfigs[i].setTimeout(serviceBean[i].getTimeout());
            interfaceConfigs[i].setVersion(serviceBean[i].getVersion());
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
        for(int i = 0;i<providerPath.length;i++){
            registryService.unregister(providerPath[i]);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
