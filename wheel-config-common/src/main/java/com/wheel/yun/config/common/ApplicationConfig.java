package com.wheel.yun.config.common;



/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/28 17:52
 */
public class ApplicationConfig {
    private String name;
    public ApplicationConfig(String name){
        this.name = name;
    }
    public ApplicationConfig(){
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
