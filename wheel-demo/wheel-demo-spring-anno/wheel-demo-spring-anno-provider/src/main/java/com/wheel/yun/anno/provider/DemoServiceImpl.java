package com.wheel.yun.anno.provider;

import com.wheel.yun.demo.api.DemoService;
import org.springframework.stereotype.Service;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/10 15:31
 */
@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String hello) {
        return hello + "---->注解模式";
    }
}
