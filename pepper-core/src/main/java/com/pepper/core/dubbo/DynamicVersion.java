package com.pepper.core.dubbo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;


/**
 * 动态设置duboo版本
 * @author mrliu
 *
 */
@Deprecated
public class DynamicVersion {

	/**
	 * 重置DUBBO version
	 * @param reference
	 * @param service
	 */
	public static synchronized void resetVersion(Reference reference,Service service,Class<?> beanClass,Field field){
		InvocationHandler invocationHandler = null;
		if(reference!=null){
			invocationHandler = Proxy.getInvocationHandler(reference);
		}else if(service!=null){
			invocationHandler = Proxy.getInvocationHandler(service);
		}
		Field declaredField;
		try {
			declaredField = invocationHandler.getClass().getDeclaredField("memberValues");
			declaredField.setAccessible(true);
			@SuppressWarnings("unchecked")
			Map<String, Object> memberValues = (Map<String, Object>) declaredField.get(invocationHandler);
			String currentVserion = memberValues.get("version")==null?"":memberValues.get("version").toString();
			if(!currentVserion.equals("*")){
				memberValues.put("version", getVserion(beanClass,field,reference));
			}
			System.out.println(beanClass!=null?beanClass.getName():field.getType().getName() + "------" +memberValues.get("version"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static synchronized String getVserion(Class<?> beanClass,Field field,Reference reference){
		String dubboFrameworkVersion = System.getProperty("dubbo-framework.version")!=null?System.getProperty("dubbo-framework.version"):"0.0.1";
		if(System.getProperty("isFramework")!=null&&System.getProperty("isFramework").equals("true")){
			return dubboFrameworkVersion;
		}
		
		if(field!=null&&field.getType().getName().indexOf("basement")>-1){
			return dubboFrameworkVersion;
		}else if(reference!=null&&reference.interfaceName().indexOf("basement")>-1){
			return dubboFrameworkVersion;
		}
//		else if(beanClass!=null&&beanClass.getName().indexOf("basement")>-1){
//			return dubboFrameworkVersion;
//		}
		
		
		if (System.getProperty("environment") != null && System.getProperty("environment").equals("dev")){
			return System.getProperty("dubbo.dynamic.version");
		}else{
			return "0.0.1";
		}
	}
	
}
