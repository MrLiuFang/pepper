package com.pepper.core.dubbo;
import java.lang.reflect.Method;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.AppResponse;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pepper.core.exception.BusinessException;

/**
 * ExceptionInvokerFilter
 * <p>
 * 功能：
 * <ol>
 * <li>不期望的异常打ERROR日志（Provider端）<br>
 *     不期望的日志即是，没有的接口上声明的Unchecked异常。
 * <li>异常不在API包中，则Wrap一层RuntimeException。<br>
 *     RPC对于第一层异常会直接序列化传输(Cause异常会String化)，避免异常在Client出不能反序列化问题。
 * </ol>
 *
 * @author william.liangf
 * @author ding.lid
 */
@Activate(group = CommonConstants.PROVIDER)
public class DubboExceptionFilter implements Filter {

	private final Logger logger;

	public DubboExceptionFilter() {
		this(LoggerFactory.getLogger(DubboExceptionFilter.class));
	}

	public DubboExceptionFilter(Logger logger) {
		this.logger = logger;
	}

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		try {
			Result result = invoker.invoke(invocation);
			if (result.hasException() && GenericService.class != invoker.getInterface()) {
				try {
					Throwable exception = result.getException();

					// 如果是checked异常，直接抛出
					if (! (exception instanceof RuntimeException) && (exception instanceof Exception)) {
						return result;
					}
					// 在方法签名上有声明，直接抛出
					try {
						Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
						Class<?>[] exceptionClassses = method.getExceptionTypes();
						for (Class<?> exceptionClass : exceptionClassses) {
							if (exception.getClass().equals(exceptionClass)) {
								return result;
							}
						}
					} catch (NoSuchMethodException e) {
						return result;
					}

					// 未在方法签名上定义的异常，在服务器端打印ERROR日志
					logger.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost()
							+ ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
							+ ", exception: " + exception.getClass().getName() + ": " + exception.getMessage(), exception);

					// 异常类和接口类在同一jar包里，直接抛出
					String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
					String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
					if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)){
						return result;
					}
					// 是JDK自带的异常，直接抛出
					String className = exception.getClass().getName();
					if (className.startsWith("java.") || className.startsWith("javax.")) {
						return result;
					}
					// 是Dubbo本身的异常，直接抛出
					if (exception instanceof RpcException || exception instanceof BusinessException) {
						return result;
					}
					
					// 否则，包装成RuntimeException抛给客户端
					return new AppResponse(new RuntimeException(StringUtils.toString(exception)));
				} catch (Throwable e) {
					logger.warn("Fail to ExceptionFilter when called by " + RpcContext.getContext().getRemoteHost()
							+ ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
							+ ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
					return result;
				}
			}
			return result;
		} catch (RuntimeException e) {
			logger.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost()
					+ ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
					+ ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
			throw e;
		}
	}

}