package com.wheel.yun.config.spring.processor;

import java.util.List;

public interface ConfigPostProcessor {
    List<Class> getConfigPostProcessorList();
}
