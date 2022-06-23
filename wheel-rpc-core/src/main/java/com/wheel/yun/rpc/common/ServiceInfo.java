package com.wheel.yun.rpc.common;


import java.io.Serializable;


public class ServiceInfo implements Serializable {

	/**
     *  应用名称
	 */
	private String appName;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	/**
     *  服务名称
	 */
	private String serviceName;

	/**
	 *  版本
	 */
	private String version;

	/**
     *  地址
	 */
	private String address;

    /**
     *  端口
	 */
	private Integer port;
}
