package com.wheel.yun.config.spring.processor.params;

import com.wheel.yun.config.common.ProtocolConfig;
import com.wheel.yun.config.spring.context.ConfigsParamsContext;
import com.wheel.yun.config.spring.processor.ConfigsParamsPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/8/3 10:56
 */
public class ProtocolParamsPostProcessor implements ConfigsParamsPostProcessor {

    public ProtocolParamsPostProcessor(){
        this.postProcessConfigParams();
    }

    @Override
    public void postProcessConfigParams() {
        List<String> params = this.getParams();
        ConfigsParamsContext.putParams(ProtocolConfig.class,params);
        this.getBeanName();
    }

    @Override
    public List<String> getParams() {
        List<String> params = new ArrayList<>();
        params.add("protocol");
        params.add("port");
        return params;
    }

    @Override
    public void getBeanName() {
        List<String> ids = new ArrayList<>();
        ids.add("protocol");
        ConfigsParamsContext.putClassAndBeanName(ProtocolConfig.class,ids);
    }
}
