package com.pepper.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author mrliu
 *
 */
public class LoginTokenUtil {

	public synchronized static String getLoginToken(final String tokenName) {
		String token =null;
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if(requestAttributes == null) {
			return null;
		}
		HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
		if(request == null) {
			return null;
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(tokenName)) {
					return cookie.getValue();
				}
			}
		}
		//从header获取token
		if (!StringUtils.hasText(token) && request.getHeader(tokenName) != null) {
			token = request.getHeader(tokenName).toString();
		}
		// 获取参数
		if(!StringUtils.hasText(token) && request.getParameter(tokenName) != null){
			token = request.getParameter(tokenName);
		}
		
		return token;
	}
}
