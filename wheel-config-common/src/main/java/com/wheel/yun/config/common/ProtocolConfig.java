package com.wheel.yun.config.common;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/30 15:58
 */
public class ProtocolConfig {
    private String protocol;
    private Integer port;

    private String timeOut;

    public void setTimeOut(String timeOut){
        this.timeOut = timeOut;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getTimeOut() {
        return this.timeOut;
    }
}
