package com.pepper.core.dubbo;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
/**
 * 
 * @author mrliu
 *
 */
@Activate(group = Constants.CONSUMER)
public class TraceConsumerFilter implements Filter{
	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		try{
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			if(request != null){
				Map<String,String> map = invocation.getAttachments();
				map.put(TraceContext.DOMAIN, request.getServerName());
				TraceContextUtil.setDomain(map.get(TraceContext.DOMAIN));
			}
		}catch (Exception e) {
		}
		TraceContextUtil.setTraceId(invocation);
		return invoker.invoke(invocation);
	}

}
