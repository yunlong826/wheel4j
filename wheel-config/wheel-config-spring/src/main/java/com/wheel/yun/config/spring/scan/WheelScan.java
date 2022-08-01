package com.wheel.yun.config.spring.scan;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(WheelScannerRegistrar.class)
@Documented
public @interface WheelScan {
    String[] basePackage();
}
