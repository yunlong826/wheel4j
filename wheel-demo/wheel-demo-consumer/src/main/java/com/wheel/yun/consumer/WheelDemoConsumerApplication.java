package com.wheel.yun.consumer;

import com.wheel.yun.api.DemoService;


import com.wheel.yun.config.spring.annonation.Reference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WheelDemoConsumerApplication {

    @Reference
    private DemoService demoService;

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("wheel-consumer.xml");
        WheelDemoConsumerApplication wheelDemoXmlConsumerApplication = applicationContext.getBean("wheelDemoConsumerApplication", WheelDemoConsumerApplication.class);
        System.out.println(wheelDemoXmlConsumerApplication.doSayHello("111111111"));
    }
    public String doSayHello(String s){
        return demoService.sayHello(s);
    }

}
