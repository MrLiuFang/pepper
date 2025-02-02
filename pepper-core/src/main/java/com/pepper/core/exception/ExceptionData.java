package com.pepper.core.exception;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.dubbo.rpc.RpcException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.pepper.core.ResultData;
import com.pepper.core.ResultEnum;

/**
 * 
 * @author mrliu
 *
 */
public class ExceptionData {

	/**
	 * 异常时设置返回统一错误信息
	 *
	 * @param e
	 *            Throwable
	 * @return ResultData
	 */
	public Object getResultData(Throwable e, Class<?> returnType, ResponseBody methodResponseBody, ResponseBody controllerResponseBody, RestController controllerRestController) {
		String responseType = ResponseType.getResponseType(returnType, methodResponseBody, controllerResponseBody, controllerRestController);
		if(responseType.equals(ResponseType.JSON)){
			if (e instanceof BusinessException) {
				return returnResultData(e,ResultEnum.Status.LOGIC_ERROR,e.getMessage());
			} else if (e instanceof AuthorizeException) {
				return returnResultData(e,ResultEnum.Status.IS_NOT_LOGIN,e.getMessage());
			} else if (e instanceof NoPermissionException) {
				return returnResultData(e,ResultEnum.Status.NO_PERMISSION,e.getMessage());
			} else if (e instanceof RpcException) {
				return returnResultData(e,ResultEnum.Status.SYSTEM_ERROR,"程序可能开小差了,(错误代码:RpcException)");
			} else if (e instanceof UndeclaredThrowableException){
				while(e.getCause()!=null && (e instanceof InvocationTargetException ? ((InvocationTargetException) e).getTargetException()!=null : true)){
					e = e.getCause();
					if(e instanceof InvocationTargetException){
						e = ((InvocationTargetException) e).getTargetException();
					}
				}
				return returnResultData(e,ResultEnum.Status.SYSTEM_ERROR,e.getMessage());
			} else {
				return returnResultData(e,ResultEnum.Status.SYSTEM_ERROR,"程序可能开小差了,(错误代码:Unknown)");
			}
		}else if (responseType.equals(ResponseType.VIEW)) {
			HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
			response.setStatus(200);
			if (e instanceof AuthorizeException) {
				return returnView(returnType,"redirect:/notLogin",e.getMessage());
			} else if (e instanceof NoPermissionException) {
				return returnView(returnType,"redirect:/noPermission",e.getMessage());
			} else if (e instanceof BusinessException) {
				return returnView(returnType,"redirect:/500",e.getMessage());
			} else {
				return returnView(returnType,"redirect:/500",e.getMessage());
			}
		} else {
			new RuntimeException(e.getMessage(),e);
		}
		return responseType;
	}
	
	private ResultData returnResultData(Throwable e, ResultEnum.Status status, String message ){
		ResultData resultData = new ResultData();
		resultData.setStatus(status.getKey());
		resultData.setExceptionMessage(e.getMessage());
		resultData.setMessage(message);
		return resultData;
	}
	
	private Object returnView(Class<?> returnType,String viewUrl,String message){
		if(returnType.getName().equals(String.class.getName())){
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			request.setAttribute("message", message);
			return viewUrl;
		}else if(returnType.getName().equals(ModelAndView.class.getName())){
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName(viewUrl);
			modelAndView.addObject("message", message);
			return modelAndView;
		}
		return viewUrl;
	}
}
