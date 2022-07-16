package com.wheel.yun.demo.anno.consumer.service;

import com.wheel.yun.config.spring.annonation.Reference;
import com.wheel.yun.demo.api.DemoService;
import org.springframework.stereotype.Service;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/7/16 14:08
 */
@Service("demoAnnoConsumer")
public class DemoAnnoConsumer implements DemoService {

    @Reference
    private DemoService demoService;

    @Override
    public String sayHello(String hello) {
        return demoService.sayHello(hello);
    }
}
