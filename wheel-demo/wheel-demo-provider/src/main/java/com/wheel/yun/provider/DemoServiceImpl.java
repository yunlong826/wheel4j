package com.wheel.yun.provider;


import com.wheel.yun.api.DemoService;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/6/2 15:06
 */
public class DemoServiceImpl implements DemoService {
//    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);
    @Override
    public String sayHello(String name) {
//        logger.info("Hello " + name + ", request from consumer");
        return name;
    }
}
