package com.pepper.core.dubbo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor;

/**
 * 动态设置duboo版本
 * 
 * @author mrliu
 *
 */

public class DubboDynamicVersion implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0) throws BeansException {
		String[] beanStr = arg0.getBeanDefinitionNames();
		for (String beanName : beanStr) {
			//if(beanName.equals("adminUserServiceImpl")){
				BeanDefinition beanDefinition = arg0.getBeanDefinition(beanName);
				try {
					Method method = beanDefinition.getClass().getMethod("getMetadata");
					method.setAccessible(true);
					AnnotationMetadataReadingVisitor object =  (AnnotationMetadataReadingVisitor) method.invoke(beanDefinition);
					AnnotationAttributes annotationAttributes = object.getAnnotationAttributes("org.apache.dubbo.config.annotation.Service");
					if(annotationAttributes!=null){
						annotationAttributes.put("version", "1.1.1");
					}
				} catch (Exception e) {
//					e.printStackTrace();
				}
			//}
		}
	}

}
