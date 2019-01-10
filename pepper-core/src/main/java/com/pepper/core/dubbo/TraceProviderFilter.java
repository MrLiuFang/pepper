package com.pepper.core.dubbo;
import java.util.Map;

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
