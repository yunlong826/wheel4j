package com.wheel.yun.test.provider;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;


public class WheelTestProviderApplication {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("wheel-test-provider.xml");
        context.start();
        System.in.read();
    }

}
