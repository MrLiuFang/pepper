package com.pepper.core.dubbo;

import java.io.IOException;
import java.security.ProtectionDomain;
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
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;

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
		List<String> classNameList = new ArrayList<String>();
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
			dubboToMvc(classNameList);
		} catch (IOException | ClassNotFoundException | NotFoundException | CannotCompileException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void dubboToMvc(List<String> classNameList)
			throws NotFoundException, ClassNotFoundException, CannotCompileException, IOException, InstantiationException, IllegalAccessException {

		ClassPool pool = ClassPool.getDefault();
		ClassLoader loader = this.getClass().getClassLoader();
		ProtectionDomain domain = this.getClass().getProtectionDomain();
		for (String className : classNameList) {
			CtClass ct = pool.get(className);
			Service service = (Service) ct.getAnnotation(Service.class);
			if (service != null) {
				service = null;
			}
			
			CtField[] fields = ct.getDeclaredFields();
			for (CtField ctField : fields) {
				Reference reference = (Reference) ctField.getAnnotation(Reference.class);
				if (reference != null) {
					reference = null;
				}
			}
			if(!ct.isAnnotation()&&!ct.isArray()&&!ct.isEnum()&&!ct.isFrozen()&&!ct.isInterface()&&!ct.isModified()&&!ct.isPrimitive()){
				System.out.println(ct.getName());
				ct.toBytecode();
				ct.prune();
				ct.toClass(loader, domain);
			}
			ct.writeFile();
			
		}
	}

	private void dubboReferenceToMvcAutowired(CtClass ct) throws ClassNotFoundException {
		CtField[] fields = ct.getDeclaredFields();
		for (CtField ctField : fields) {
			Reference reference = (Reference) ctField.getAnnotation(Reference.class);
//			if (reference != null) {
//				reference = null;
//				FieldInfo fieldInfo = ctField.getFieldInfo();
//				ClassFile ccFile = ct.getClassFile();
//				ConstPool constpool = ccFile.getConstPool();
//				AnnotationsAttribute fieldAttr = new AnnotationsAttribute(constpool, AnnotationsAttribute.visibleTag);
//				Annotation autowired = new Annotation("org.springframework.beans.factory.annotation.Autowired",constpool);
//				fieldAttr.addAnnotation(autowired);
//				fieldInfo.addAttribute(fieldAttr);
//			}
		}
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

}
