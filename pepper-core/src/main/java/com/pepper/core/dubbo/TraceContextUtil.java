package com.pepper.core.dubbo;

import java.util.Map;
import java.util.UUID;

import org.apache.dubbo.rpc.Invocation;


/**
 * 
 * @author mrliu
 *
 */
public class TraceContextUtil {

	/**
	 * 设置调用链ID到当前线程保存起来
	 * @param invocation
	 */
	public synchronized static void setTraceId(final Invocation invocation) {
		Map<String, String> map = invocation.getAttachments();
		if (! map.containsKey(TraceContext.TRACE_ID)) {
			String traceId = UUID.randomUUID().toString();
			map.put(TraceContext.TRACE_ID, traceId);
		}
		TraceContext.setTraceId(map.get(TraceContext.TRACE_ID));
	}

	/**
	 * 获取当前调用链的ID
	 * @return
	 */
	public synchronized static String getTraceId() {
		return TraceContext.getTraceId();
	}
	
	/**
	 * 设置当前请求域名到线程
	 * @param invocation
	 */
	public synchronized static void setDomain(final String domain ){
		TraceContext.setDomain(domain);
	}

	/**
	 * 获取当前请求域名
	 * @return
	 */
	public synchronized static String getDomain() {
		return TraceContext.getDomain();
	}
	
	
}
