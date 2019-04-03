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
import com.pepper.util.CurrentUserUtil;
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
	
	@Autowired
	private CurrentUserUtil currentUserUtil;
	
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
		return currentUserUtil.getCurrentUser();
	}

}
