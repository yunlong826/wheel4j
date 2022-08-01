package com.wheel.yun.config.spring.scan;

import com.wheel.yun.config.spring.annonation.WheelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * @author jack_yun
 * @version 1.0
 * @description: 扫描自定义注解
 * @date 2022/7/16 10:11
 */
@Slf4j
public class WheelScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {


    private static final String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackage";
    private static final String SPRING_BEAN_BASE_PACKAGE = "com.wheel.yun.config.spring";

    private ResourceLoader resourceLoader;
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes wheelScanAnnotationAttributes = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(WheelScanner.class.getName()));
        String[] wheelScanBasePackages = new String[0];
        if(wheelScanAnnotationAttributes != null){
            wheelScanBasePackages =wheelScanAnnotationAttributes.getStringArray(BASE_PACKAGE_ATTRIBUTE_NAME);
        }
        if(wheelScanBasePackages.length == 0){
            wheelScanBasePackages = new String[]{((StandardAnnotationMetadata)
                    importingClassMetadata).getIntrospectedClass().getPackage().getName()};
        }

        WheelScanner wheelServiceScanner = new WheelScanner(registry, WheelService.class);
        WheelScanner springBeanScanner = new WheelScanner(registry, Component.class);

        if (resourceLoader != null) {
            wheelServiceScanner.setResourceLoader(resourceLoader);
        }

        // 扫描后，spring内部会将我们自定义注解的实例加载进spring容器里
        int springBeanAmount = springBeanScanner.scan(SPRING_BEAN_BASE_PACKAGE);
        log.info("springBeanScanner扫描的数量 [{}]", springBeanAmount);

        int wheelServiceCount = wheelServiceScanner.scan(wheelScanBasePackages);
        log.info("wheelServiceScanner扫描的数量 [{}]", wheelServiceCount);

    }
}
