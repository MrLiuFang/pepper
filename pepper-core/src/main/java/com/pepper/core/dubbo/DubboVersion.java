package com.pepper.core.dubbo;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * 动态设置Dubbo版本号，用于分布式开发
 * 工作机制使用javassist动态修改class字节码技术，更改DUBBO扫描类（类似动态代理）
 * @author mrliu
 *
 */
//@WebListener
@Deprecated
public class DubboVersion implements ServletContextListener {


	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			ClassPool pool = ClassPool.getDefault();
			pool.insertClassPath(new ClassClassPath(this.getClass()));
			CtClass clazz = pool.getCtClass("org.apache.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor");
			CtMethod	ctMethod = clazz.getDeclaredMethod("findFieldReferenceMetadata");
			ctMethod.insertAt(12, "DynamicVersion.resetVersion(reference, null, null, field);");
			clazz.toClass();  
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}
}
