package com.wheel.admin.config;

import com.wheel.admin.registry.zk.directory.RegistryDirectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 *
 * @author longyun
 * @version 1.0
 * @date 2022/7/30 19:18
 */
@Configuration
public class ZkDataConfig {
    @Value("${wheel.zk.address}")
    private String zkAddress;

    @Bean
    public RegistryDirectory registryDirectory(){
        return new RegistryDirectory(zkAddress);
    }
}
