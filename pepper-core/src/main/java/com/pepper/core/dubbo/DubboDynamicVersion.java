package com.pepper.core.dubbo;

import java.util.Set;

import org.reflections.Reflections;
import org.springframework.stereotype.Controller;

/**
 * 动态设置duboo版本
 * 
 * @author mrliu
 *
 */
public class DubboDynamicVersion {

	public DubboDynamicVersion() {
		super();
		Reflections reflections = new Reflections("com.sea.server.hessian");
		Set<Class<?>> hessianImpls = reflections.getTypesAnnotatedWith(Controller.class);
	}

}
