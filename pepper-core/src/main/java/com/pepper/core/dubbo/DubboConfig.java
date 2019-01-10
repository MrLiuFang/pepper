package com.pepper.core.dubbo;

/**
 * 
 * @author mrliu
 *
 */
public class DubboConfig {

	// 重试次数
	public static final int RETRIES = 0;

	// 超时时长
	public static final int TIMEOUT = 6000;

	// 集群容错模式：
	public static final String CLUSTER = "failfast";

	// 是否监测发布者是否在zookeeper中心注册
	public static final boolean CHECK = false;

	public static final int CONNECTIONS = 2;

}
