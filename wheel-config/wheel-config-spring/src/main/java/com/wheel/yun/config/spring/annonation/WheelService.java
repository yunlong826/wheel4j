package com.wheel.yun.config.spring.annonation;


import java.lang.annotation.*;


/**
 *
 * @author long_yun
 * @date 2022/7/16 10:01
 * @describe 提供端注解
 */
@Target({ ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WheelService {

    String group() default "";

    String version() default "1.0.0";

    String timeout() default "3000";

    String failStrategy() default "failover";

    String retryCount() default "3";

    String weight() default "0";

    String loadBalance() default "consistentHash";

    Class<?> interfaceClass() default void.class;
}
