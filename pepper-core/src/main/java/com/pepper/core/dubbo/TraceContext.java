package com.pepper.core.dubbo;

/**
 * 调用链上下文，用户追踪dubbo server调用追踪
 * 
 * @author mrliu
 *
 */
public class TraceContext {

	private static ThreadLocal<String>  _traceId = new InheritableThreadLocal<String>();
	
	private static ThreadLocal<String>  _domain = new InheritableThreadLocal<String>();
	
	public static String TRACE_ID = "TRACE_ID";
	
	public static String DOMAIN = "DOMAIN";
	

	public synchronized static String getTraceId() {
		return _traceId.get();
	}

	public synchronized static void setTraceId(String traceId) {
		_traceId.set(traceId);
	}

	public static String getDomain() {
		return _domain.get();
	}

	public static void setDomain(String domain) {
		_domain.set(domain);
	}
	
	

}
