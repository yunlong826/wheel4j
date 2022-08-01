package com.wheel.yun.demo.anno.consumer;

import com.wheel.yun.config.common.ApplicationConfig;
import com.wheel.yun.config.common.ProtocolConfig;
import com.wheel.yun.config.common.RegistryConfig;
import com.wheel.yun.config.spring.scan.WheelScan;
import com.wheel.yun.demo.anno.consumer.service.DemoAnnoConsumer;
import com.wheel.yun.demo.api.DemoService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


public class WheelDemoSpringAnnoConsumerApplication {


    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        context.start();
        DemoService demoService = context.getBean("demoAnnoConsumer", DemoAnnoConsumer.class);
        System.out.println(demoService.sayHello("这是一个注解--------"));
    }

    @Configuration
    @WheelScan(basePackage = "com.wheel.yun.demo.anno.consumer")
    @ComponentScan(value = "com.wheel.yun.demo.anno.consumer")
    static class ConsumerConfiguration {
//      注释下列信息是为了测试application.properties文件下配置下列相关信息。
//        @Bean(name = "registry2")
//        public RegistryConfig registryConfig(){
//            RegistryConfig registryConfig = new RegistryConfig();
//            registryConfig.setAddress("zookeeper://127.0.0.1:2181");
//            return registryConfig;
//        }
//
//        @Bean
//        public ApplicationConfig applicationConfig(){
//            ApplicationConfig applicationConfig = new ApplicationConfig();
//            applicationConfig.setName("demo-anno-consumer");
//            return applicationConfig;
//        }
//
//        @Bean
//        public ProtocolConfig protocolConfig(){
//            ProtocolConfig protocolConfig = new ProtocolConfig();
//            protocolConfig.setProtocol("dubbo");
//            protocolConfig.setPort(8084);
//            return protocolConfig;
//        }

    }

}
