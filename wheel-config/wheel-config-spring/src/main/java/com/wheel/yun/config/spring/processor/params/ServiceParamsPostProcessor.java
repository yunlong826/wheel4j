package com.wheel.yun.config.spring.processor.params;

import com.wheel.yun.config.spring.ServiceBean;
import com.wheel.yun.config.spring.context.ConfigsParamsContext;
import com.wheel.yun.config.spring.processor.ConfigsParamsPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/8/3 11:00
 */
public class ServiceParamsPostProcessor implements ConfigsParamsPostProcessor {

   public ServiceParamsPostProcessor(){
       this.postProcessConfigParams();
   }

    @Override
    public void postProcessConfigParams() {
        List<String> params = this.getParams();
        ConfigsParamsContext.putParams(ServiceBean.class,params);
        this.getBeanName();
    }

    @Override
    public List<String> getParams() {
        List<String> params = new ArrayList<>();
        params.add("interface");
        params.add("version");
        params.add("ref");
        params.add("group");
        params.add("timeout");
        params.add("failStrategy");
        params.add("retryCount");
        params.add("weight");
        params.add("loadbalance");
        return params;
    }

    @Override
    public void getBeanName() {
        List<String> ids = new ArrayList<>();
        ids.add("interface");
        ConfigsParamsContext.putClassAndBeanName(ServiceBean.class,ids);
    }
}
