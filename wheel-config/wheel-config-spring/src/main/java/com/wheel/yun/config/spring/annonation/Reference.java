package com.wheel.yun.config.spring.annonation;


import java.lang.annotation.*;

/*
 *
 * @author long_yun
 * @date 2022/6/4 13:52
 * @describe 消费端注解
 */
@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Reference {

    String group() default "";

    String version() default "1.0.0";

    String timeout() default "3000";

    String failStrategy() default "failover";

    String retryCount() default "3";
}
