package com.pepper.core.base.impl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.pepper.core.base.BaseController;
/**
 * 
 * @author mrliu
 *
 */

public abstract class BaseControllerImpl implements BaseController {
	
	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;
	

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
		return null;
	}

}
