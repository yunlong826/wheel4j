package com.wheel.yun.config.spring.processor.params;

import com.wheel.yun.config.spring.ReferenceBean;
import com.wheel.yun.config.spring.context.ConfigsParamsContext;
import com.wheel.yun.config.spring.processor.ConfigsParamsPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/8/3 10:58
 */
public class ReferenceParamsPostProcessor implements ConfigsParamsPostProcessor {


    public ReferenceParamsPostProcessor(){
        this.postProcessConfigParams();
    }

    @Override
    public void postProcessConfigParams() {
        List<String> params = this.getParams();
        ConfigsParamsContext.putParams(ReferenceBean.class,params);
        this.getBeanName();
    }

    @Override
    public List<String> getParams() {
        List<String> params = new ArrayList<>();
        params.add("id");
        params.add("interface");
        params.add("version");
        params.add("group");
        params.add("timeout");
        params.add("failStrategy");
        params.add("retryCount");
        return params;
    }

    @Override
    public void getBeanName() {
        List<String> ids = new ArrayList<>();
        ids.add("id");
        ids.add("interface");
        ids.add("version");
        ids.add("group");
        ConfigsParamsContext.putClassAndBeanName(ReferenceBean.class,ids);

    }
}
