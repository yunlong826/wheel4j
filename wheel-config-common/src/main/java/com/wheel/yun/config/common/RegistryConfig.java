package com.wheel.yun.config.common;



import com.wheel.yun.common.URL;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/28 17:59
 */
public class RegistryConfig implements Serializable {

    public RegistryConfig(){}

    public RegistryConfig(String address){
        this.address = address;
        setAddress(address);
    }
    public RegistryConfig(String address,String id){
        this.address = address;
        this.id = id;
        setAddress(address);
    }


    /**
     * Register center address
     */
    private String address;

    /**
     * Register center id
     */
    private String id;

    /**
     * Username to login register center
     */
    private String username;

    /**
     * Password to login register center
     */
    private String password;

    /**
     * host
     */
    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Default port for register center
     */
    private Integer port;

    /**
     * Protocol for register center
     */
    private String protocol;

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
        if(address!=null){
            URL url = URL.valueOf(address);

            updatePropertyIfAbsent(this::getUsername,this::setUsername,url.getUserName());
            updatePropertyIfAbsent(this::getPassword,this::setPassword,url.getPassWord());
            updatePropertyIfAbsent(this::getPort,this::setPort,url.getPort());
            updatePropertyIfAbsent(this::getHost,this::setHost,url.getHost());
            updatePropertyIfAbsent(this::getPath,this::setPath,url.getPath());

        }
    }
    public static <T> void updatePropertyIfAbsent(Supplier<T> getterMethod, Consumer<T> setterMethod, T newValue) {
        if (newValue != null && getterMethod.get() == null) {
            setterMethod.accept(newValue);
        }
    }
}
