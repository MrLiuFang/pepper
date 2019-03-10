package com.pepper.core.dubbo;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.reflections.Reflections;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

/**
 * 
 * @author Mr.Liu
 *
 */
public class DubboDynamicVersionRegistrar implements ImportBeanDefinitionRegistrar {

	@SuppressWarnings("unchecked")
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		try {
//			readDubboVsersion();
			Reflections reflections = new Reflections(convertPackage(getPackagesToScan(importingClassMetadata)));
			Set<Class<?>> services = reflections.getTypesAnnotatedWith(Service.class);
			for (Class<?> classz : services) {
				Service service = (Service) classz.getAnnotation(Service.class);
				if (service != null) {
					InvocationHandler invocationHandler = Proxy.getInvocationHandler(service);
					Field value = invocationHandler.getClass().getDeclaredField("memberValues");
					value.setAccessible(true);
					Map<String, Object> memberValues = (Map<String, Object>) value.get(invocationHandler);
					memberValues.put("version", "1.2.3");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void readDubboVsersion() throws FileNotFoundException {
		File file = ResourceUtils.getFile("classpath:META-INF/dubboVsersion.properties");
		System.out.println("1");
	}

	/**
	 * 转化package路径
	 * 
	 * @param packages
	 * @return
	 */
	private Object[] convertPackage(Set<String> packagesToScan) {
		List<String> listPackage = new ArrayList<String>();
		for (String str : packagesToScan) {
			str = str.replace(".**", "");
			if (!listPackage.contains(str)) {
				listPackage.add(str);
			}
		}
		return listPackage.toArray();
	}

	private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
		AnnotationAttributes attributes = AnnotationAttributes
				.fromMap(metadata.getAnnotationAttributes(DubboComponentScan.class.getName()));
		String[] basePackages = attributes.getStringArray("basePackages");
		Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
		String[] value = attributes.getStringArray("value");
		// Appends value array attributes
		Set<String> packagesToScan = new LinkedHashSet<String>(Arrays.asList(value));
		packagesToScan.addAll(Arrays.asList(basePackages));
		for (Class<?> basePackageClass : basePackageClasses) {
			packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
		}
		if (packagesToScan.isEmpty()) {
			return Collections.singleton(ClassUtils.getPackageName(metadata.getClassName()));
		}
		return packagesToScan;
	}

}
