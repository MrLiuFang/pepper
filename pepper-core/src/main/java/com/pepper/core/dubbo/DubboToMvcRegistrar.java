package com.pepper.core.dubbo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

/**
 * 
 * @author mrliu
 *
 */
public class DubboToMvcRegistrar
		implements ImportBeanDefinitionRegistrar, EnvironmentAware, Ordered, ResourceLoaderAware {

	@SuppressWarnings("unused")
	private Environment environment;

	private ResourceLoader resourceLoader;

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;

	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		String[] packages = PackagesScanUtil
				.convertPackage(PackagesScanUtil.packagesToScan(importingClassMetadata, registry));
		List<String> classNameList = searchClassName(packages);
		ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
		Resource[] resources;
		try {
			for (String str : packages) {
				resources = resolver.getResources("classpath*:" + str.replace(".", "/") + "/**/*.class");
				for (Resource r : resources) {
					MetadataReader reader = metaReader.getMetadataReader(r);
					String className = reader.getClassMetadata().getClassName();
					classNameList.add(className);
				}
			}
			dubboServiceToMvcService(classNameList);
			dubboReferenceToMvcAutowired(classNameList);
			System.out.println("1");

		} catch (IOException | ClassNotFoundException | NotFoundException | CannotCompileException e) {
			e.printStackTrace();
		}
	}

	private void dubboServiceToMvcService(List<String> classNameList) throws NotFoundException, ClassNotFoundException, CannotCompileException {
		
		ClassPool pool = ClassPool.getDefault();
		for(String className : classNameList){
			CtClass ct = pool.get(className);
			Service service = (Service) ct.getAnnotation(Service.class);
			if(service != null){
				System.out.println(className);
				dubboReferenceToMvcAutowired(ct);
				ct.toClass();
			}
		}
//		// 获取需要修改的类
//		CtClass ct = pool.get("com.tgb.itoo.collection.base.CollectionBase");
//
//		// 获取类里的所有方法
//		CtMethod[] cms = ct.getDeclaredMethods();
//		CtMethod cm = cms[0];
//		System.out.println("方法名称====" + cm.getName());
//
//		MethodInfo minInfo = cm.getMethodInfo();
//		// 获取类里的em属性
//		CtField cf = ct.getField("em");
//		FieldInfo fieldInfo = cf.getFieldInfo();
//
//		System.out.println("属性名称===" + cf.getName());
//
//		ConstPool cp = fieldInfo.getConstPool();
//		// 获取注解信息
//		AnnotationsAttribute attribute2 = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
//		Annotation annotation = new Annotation("javax.persistence.PersistenceContext", cp);
//
//		// 修改名称为unitName的注解
//		annotation.addMemberValue("unitName", new StringMemberValue("basic-entity", cp));
//		attribute2.setAnnotation(annotation);
//		minInfo.addAttribute(attribute2);
//
//		// 打印修改后方法
//		Annotation annotation2 = attribute2.getAnnotation("javax.persistence.PersistenceContext");
//		String text = ((StringMemberValue) annotation2.getMemberValue("unitName")).getValue();
//
//		System.out.println("修改后的注解名称===" + text);
	}

	private void dubboReferenceToMvcAutowired(List<String> classNameList) throws NotFoundException, ClassNotFoundException, CannotCompileException {
		ClassPool pool = ClassPool.getDefault();
		for(String className : classNameList){
			CtClass ct = pool.get(className);
			Service service = (Service) ct.getAnnotation(Service.class);
			if( service == null){
				dubboReferenceToMvcAutowired(ct);
				ct.toClass();
			}
		}
	}
	
	private void dubboReferenceToMvcAutowired(CtClass ct) throws ClassNotFoundException {
		CtField[] fields = ct.getDeclaredFields();
		for(CtField  ctField : fields){
			Reference reference =(Reference) ctField.getAnnotation(Reference.class);
			if(reference != null){
				
			}
		}
	}

	private List<String> searchClassName(final String[] packages) {
		List<String> classNameList = new ArrayList<String>();

		return classNameList;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
