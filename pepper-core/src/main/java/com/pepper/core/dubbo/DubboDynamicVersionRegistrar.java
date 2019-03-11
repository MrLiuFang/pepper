package com.pepper.core.dubbo;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.RandomUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.reflections.Reflections;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * 
 * @author Mr.Liu
 *
 */
public class DubboDynamicVersionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware{
	
	private MultiValueMap<String, String> version = new LinkedMultiValueMap<>();
	
	private Environment environment;
	
	private String dynamicVersion = String.valueOf(RandomUtils.nextInt(1, 10000));

	public DubboDynamicVersionRegistrar() throws IOException {
		super();
		ClassLoader classLoader = DubboDynamicVersionRegistrar.class.getClassLoader();
		Enumeration<URL> urls = (classLoader != null ?
				classLoader.getResources("META-INF/dubboVersion.properties") :
				ClassLoader.getSystemResources("META-INF/dubboVersion.properties"));
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			UrlResource resource = new UrlResource(url);
			Properties properties = PropertiesLoaderUtils.loadProperties(resource);
			for (Map.Entry<?, ?> entry : properties.entrySet()) {
				String factoryClassName = ((String) entry.getKey()).trim();
				for (String factoryName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) {
					version.add(factoryClassName, factoryName.trim());
				}
			}
		}
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		try {
			AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(DubboComponentScan.class.getName()));
			Set<String> packagesToScan = getPackagesToScan(attributes,importingClassMetadata,"basePackages","basePackageClasses");
			attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(SpringBootApplication.class.getName()));
			packagesToScan.addAll(getPackagesToScan(attributes,importingClassMetadata,"scanBasePackages","scanBasePackageClasses"));
			Reflections reflections = new Reflections(convertPackage(packagesToScan));
			Set<Class<?>> service = reflections.getTypesAnnotatedWith(Service.class);
			dynamicServiceVersion(service);
			Set<Class<?>> controller = reflections.getTypesAnnotatedWith(Controller.class);
			Set<Class<?>> component = reflections.getTypesAnnotatedWith(Component.class);
			service.addAll(reflections.getTypesAnnotatedWith(org.springframework.stereotype.Service.class));
			dynamicReferenceVersion(controller);
			dynamicReferenceVersion(component);
			dynamicReferenceVersion(service);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void dynamicServiceVersion(Set<Class<?>> service) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		for (Class<?> classz : service) {
			Service serviceAnnotation= (Service) classz.getAnnotation(Service.class);
			if (serviceAnnotation != null) {
				InvocationHandler invocationHandler = Proxy.getInvocationHandler(serviceAnnotation);
				String interfacesName = serviceAnnotation.interfaceClass().getName();
				setVersion(invocationHandler, interfacesName);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setVersion(InvocationHandler invocationHandler, String interfaceName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		Field value = invocationHandler.getClass().getDeclaredField("memberValues");
		value.setAccessible(true);
		Map<String, Object> memberValues = (Map<String, Object>) value.get(invocationHandler);
		memberValues.put("version", getVersion(interfaceName));
	}
	
	private void dynamicReferenceVersion(Set<Class<?>> beans) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		for (Class<?> classz : beans) {
			Field[] fields = classz.getDeclaredFields();
			for(Field field : fields){
				if(field.getType().isInterface()){
					Reference reference = field.getAnnotation(Reference.class);
					if (reference != null) {
						InvocationHandler invocationHandler = Proxy.getInvocationHandler(reference);
						setVersion(invocationHandler, field.getType().getName());
					}
				}
			}
			Class<?> superclass = classz.getSuperclass();
			if(superclass != null){
				Set<Class<?>> classs = new LinkedHashSet<Class<?>>();
				classs.add(superclass);
				dynamicReferenceVersion(classs);
			}
		}
	}
	
	private String getVersion(String interfaceClass){
		List<String> list = version.get(interfaceClass);
		String dubboVersion = "";
		if(list != null && list.size() == 1){
			dubboVersion = list.get(0);
			if(StringUtils.hasText(dubboVersion)){
				return dubboVersion;
			}
		}else{
			if(environment.getProperty("environment","").toLowerCase().equals("dev")){
				return dynamicVersion;
			}else{
				return "1.0.0";
			}
		}
		return dubboVersion;
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
	


	private Set<String> getPackagesToScan(AnnotationAttributes attributes,AnnotationMetadata metadata,String packages, String packageClasses) {
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

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
}
