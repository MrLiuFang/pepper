package com.pepper.core.base;

/**
 * 
 * @author liufang
 *
 */
public interface BaseController {

	/**
	 * 获取cookie
	 * 
	 * @param name
	 * @return
	 */
	public String getCookie(String name);
	
	/**
	 * 获取当前登录用户，由于每个端的用户模型不一样所以返回OBJECT 使用时进行类型转换
	 * @return
	 */
	public Object getCurrentUser();
	
	
	

}
