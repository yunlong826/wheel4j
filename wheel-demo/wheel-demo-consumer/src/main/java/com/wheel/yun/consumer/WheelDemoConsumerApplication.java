package com.wheel.yun.consumer;

import com.wheel.yun.api.DemoService;


import com.wheel.yun.api.DemoTwoService;
import com.wheel.yun.config.spring.annonation.Reference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WheelDemoConsumerApplication {



    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("wheel-consumer.xml");
//        WheelDemoConsumerApplication wheelDemoXmlConsumerApplication = applicationContext.getBean("wheelDemoConsumerApplication", WheelDemoConsumerApplication.class);
//        System.out.println(wheelDemoXmlConsumerApplication.doSayHello("111111111"));
        DemoService demoService = applicationContext.getBean("demoService",DemoService.class);
        DemoTwoService demoService2 = applicationContext.getBean("demoService2", DemoTwoService.class);
        System.out.println(demoService.sayHello("111111111111111"));
        System.out.printf(demoService2.sayHello("111111111111111"));
    }


}
