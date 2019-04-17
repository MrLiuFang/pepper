package com.pepper.core.dubbo;
import java.util.Map;

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
@Activate(group = Constants.PROVIDER)
public class TraceProviderFilter implements Filter {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		Map<String,String> map = invocation.getAttachments();
		if(map.containsKey(TraceContext.DOMAIN)){
			TraceContextUtil.setDomain(map.get(TraceContext.DOMAIN));
		}
		TraceContextUtil.setTraceId(invocation);
		return invoker.invoke(invocation);
	}

}
