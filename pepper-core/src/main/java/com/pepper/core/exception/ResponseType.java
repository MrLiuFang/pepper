package com.pepper.core.exception;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 判断controller返回JSON还是页面
 * @author mrliu
 *
 */
public class ResponseType {

	public static final String JSON = "JSON";
	
	public static final String VIEW = "VIEW";
	
	/**
	 * 
	 * @param returnType
	 * @param methodResponseBody
	 * @param controllerResponseBody
	 * @param controllerRestController
	 * @return
	 */
	public static synchronized String getResponseType(final Class<?> returnType, final ResponseBody methodResponseBody, final ResponseBody controllerResponseBody, final RestController controllerRestController){
		if(methodResponseBody !=null ){
			return JSON;
		}else if(controllerResponseBody!=null || controllerRestController!= null){
			if( returnType.getName().equals(String.class.getName()) || returnType.getName().equals(ModelAndView.class.getName())){
				return VIEW;
			}else{
				return JSON;
			}
		}else{
			return VIEW;
		}
	}
		
}
