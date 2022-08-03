package com.wheel.yun.config.spring.processor;




import java.util.List;


public interface ConfigsParamsPostProcessor {
    void postProcessConfigParams();

    List<String> getParams();

    void getBeanName();
}
