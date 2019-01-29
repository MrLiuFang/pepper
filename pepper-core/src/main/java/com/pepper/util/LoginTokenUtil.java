package com.pepper.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author mrliu
 *
 */
public class LoginTokenUtil {

	public synchronized static String getLoginToken(String tokenName) {
		String token =null;
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(tokenName)) {
					return cookie.getValue();
				}
			}
		}
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
