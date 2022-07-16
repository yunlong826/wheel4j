package com.wheel.yun.anno.provider;


import com.wheel.yun.config.common.ApplicationConfig;
import com.wheel.yun.config.common.ProtocolConfig;
import com.wheel.yun.config.common.RegistryConfig;
import com.wheel.yun.config.spring.scan.WheelScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;


public class WheelDemoSpringAnnoProviderApplication {
	public static void main(String[] args) throws IOException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProviderConfiguration.class);
		context.start();
		System.in.read();
	}


	@Configuration
	@WheelScan(basePackage = "com.wheel.yun.anno.provider")
	@ComponentScan(value = "com.wheel.yun.anno.provider")
	static class ProviderConfiguration {

		@Bean(name = "registry1")
		public RegistryConfig registryConfig(){
			RegistryConfig registryConfig = new RegistryConfig();
			registryConfig.setAddress("zookeeper://127.0.0.1:2181");
			return registryConfig;
		}

		@Bean
		public ApplicationConfig applicationConfig(){
			ApplicationConfig applicationConfig = new ApplicationConfig();
			applicationConfig.setName("demo-anno-provider");
			return applicationConfig;
		}

		@Bean
		public ProtocolConfig protocolConfig(){
			ProtocolConfig protocolConfig = new ProtocolConfig();
			protocolConfig.setProtocol("dubbo");
			protocolConfig.setPort(8084);
			return protocolConfig;
		}

	}


}