package com.pepper.core.exception;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import javax.xml.validation.Validator;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 统一异常处理
 *
 * @author mrliu
 *
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionAspect {
	private static Logger logger = LoggerFactory.getLogger(ExceptionAspect.class);

	@Around(value = "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public Object around(ProceedingJoinPoint pjp) {
		Object invokeResult = null;
		Object controllerClass = pjp.getTarget();
		Signature signature = pjp.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		ResponseBody methodResponseBody = method.getAnnotation(ResponseBody.class);
		ResponseBody controllerResponseBody = controllerClass.getClass().getAnnotation(ResponseBody.class);
		RestController controllerRestController = controllerClass.getClass().getAnnotation(RestController.class);
		try {
			invokeResult = pjp.proceed();
		} catch (Throwable e) {
			ExceptionData exceptionData = new ExceptionData();
			Object o = exceptionData.getResultData(e, method.getReturnType(), methodResponseBody, controllerResponseBody, controllerRestController);
			e.printStackTrace();
			exceptionData = null;
			return o;
		}
		return invokeResult;
	}
	

}
