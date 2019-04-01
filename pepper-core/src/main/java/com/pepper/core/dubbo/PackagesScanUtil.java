package com.pepper.core.dubbo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

public class PackagesScanUtil {
	

	public static Set<String> packagesToScan(final AnnotationMetadata importingClassMetadata, final BeanDefinitionRegistry registry){
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(DubboComponentScan.class.getName()));
		Set<String> packagesToScan = getPackagesToScan(attributes,importingClassMetadata,"basePackages","basePackageClasses");
		attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(SpringBootApplication.class.getName()));
		packagesToScan.addAll(getPackagesToScan(attributes,importingClassMetadata,"scanBasePackages","scanBasePackageClasses"));
//		attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableDubbo.class.getName()));
//		packagesToScan.addAll(getPackagesToScan(attributes,importingClassMetadata,"scanBasePackages","scanBasePackageClasses"));
		return packagesToScan;
	}
	
	/**
	 * 转化package路径
	 * 
	 * @param packages
	 * @return
	 */
	public static String[] convertPackage(final Set<String> packagesToScan) {
		List<String> listPackage = new ArrayList<String>();
		for (String str : packagesToScan) {
			str = str.replace(".**", "");
			if (!listPackage.contains(str)) {
				listPackage.add(str);
			}
		}
		return listPackage.toArray(new String[listPackage.size()]);
	}
	


	private static Set<String> getPackagesToScan(final AnnotationAttributes attributes,final AnnotationMetadata metadata,final String packages, final String packageClasses) {
		String[] basePackages = attributes.getStringArray(packages);
		Class<?>[] basePackageClasses = attributes.getClassArray(packageClasses);
		String[] value = {};
		if(attributes.containsKey("value")){
			value = attributes.getStringArray("value");
		}
		Set<String> packagesToScan =  new LinkedHashSet<String>();
		packagesToScan = new LinkedHashSet<String>(Arrays.asList(value));
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
