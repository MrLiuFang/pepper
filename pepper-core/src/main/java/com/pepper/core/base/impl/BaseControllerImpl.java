package com.pepper.core.base.impl;

import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.utils.ReferenceConfigCache;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.pepper.core.Pager;
import com.pepper.core.ResultData;
import com.pepper.core.base.BaseController;
import com.pepper.core.base.BaseService;
import com.pepper.core.constant.GlobalConstant;
import com.pepper.core.dubbo.DubboDynamicVersionRegistrar;
import com.pepper.core.exception.AuthorizeException;
import com.pepper.util.LoginTokenUtil;
import com.pepper.util.SpringContextUtil;


/**
 * 
 * @author mrliu
 *
 */

public abstract class BaseControllerImpl implements BaseController{

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;
	
	@Resource
	private ApplicationConfig applicationConfig;
	
	@Resource
	private RegistryConfig registryConfig;
	
	@Override
	public String getCookie(String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	@Override
	public Object getCurrentUser() {
		ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
		reference.setApplication(applicationConfig);
		reference.setRegistry(registryConfig);
		reference.setInterface("com.pepper.service.redis.string.serializer.ValueOperationsService");
		if(DubboDynamicVersionRegistrar.version.containsKey("com.pepper.service.redis.string.serializer.ValueOperationsService")){
			reference.setVersion(DubboDynamicVersionRegistrar.version.get("com.pepper.service.redis.string.serializer.ValueOperationsService").get(0));
		}
		reference.setGeneric(true); // 声明为泛化接口
		ReferenceConfigCache cache = ReferenceConfigCache.getCache();
		GenericService genericService = cache.get(reference);
		String token = LoginTokenUtil.getLoginToken(GlobalConstant.AUTHORIZE_TOKEN);
		if(token==null || !StringUtils.hasText(token.toString())){
			return null;
		}
		// 基本类型以及Date,List,Map等不需要转换，直接调用
		Object scope = genericService.$invoke("get", new String[] { Object.class.getName() },new Object[] { token + GlobalConstant.AUTHORIZE_TOKEN_SCOPE });
		if (scope==null || !StringUtils.hasText(scope.toString())) {
			return null;
		}
		Object authorizeFactory = SpringContextUtil.getBean("authorizeFactoryBean");
		try {
			Method getAuthorize = authorizeFactory.getClass().getMethod("getAuthorize",String.class);
			Object IAuthorize = getAuthorize.invoke(authorizeFactory,String.valueOf(scope));
			Method getCurrentUser = IAuthorize.getClass().getMethod("getCurrentUser",String.class);
			Object user = getCurrentUser.invoke(IAuthorize,token);
			if(user == null){
				throw new AuthorizeException("登录超时!请重新登录!");
			}
			return user;
		} catch (Exception e) {
			throw new AuthorizeException("登录超时!请重新登录!");
		}
	}

}
