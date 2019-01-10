package com.pepper.core.dubbo;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
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
