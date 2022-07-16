package com.wheel.yun.config.spring.scan;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;

/**
 * @author jack_yun
 * @version 1.0
 * @description: 自定义扫描注解类（借助spring容器，进行自定义扫描注解）
 * @date 2022/7/16 10:08
 */
public class WheelScanner extends ClassPathBeanDefinitionScanner {

    public WheelScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> annoType){
        super(registry);
        super.addIncludeFilter(new AnnotationTypeFilter(annoType));
    }

    @Override
    public int scan(String... basePackages) {
        return super.scan(basePackages);
    }
}
