package com.wheel.yun.provider;

import com.wheel.yun.api.DemoTwoService;

public class DemoTwoServiceImpl implements DemoTwoService {
    @Override
    public String sayHello(String name) {
        return name+"this is two service";
    }
}
