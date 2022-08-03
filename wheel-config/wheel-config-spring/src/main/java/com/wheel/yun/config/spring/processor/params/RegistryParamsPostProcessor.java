package com.wheel.yun.config.spring.processor.params;



import com.wheel.yun.config.common.RegistryConfig;
import com.wheel.yun.config.spring.context.ConfigsParamsContext;
import com.wheel.yun.config.spring.processor.ConfigsParamsPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/8/3 10:54
 */
public class RegistryParamsPostProcessor implements ConfigsParamsPostProcessor {


    public RegistryParamsPostProcessor(){
        this.postProcessConfigParams();
    }


    @Override
    public void postProcessConfigParams() {
        List<String> params = this.getParams();
        ConfigsParamsContext.putParams(RegistryConfig.class,params);
        this.getBeanName();
    }

    @Override
    public List<String> getParams() {
        List<String> params = new ArrayList<>();
        params.add("id");
        params.add("address");
        return params;
    }

    @Override
    public void getBeanName() {
        List<String> ids = new ArrayList<>();
        ids.add("id");
        ConfigsParamsContext.putClassAndBeanName(RegistryConfig.class,ids);
    }
}
