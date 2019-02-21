package com.pepper.core.exception;

import org.apache.dubbo.rpc.RpcException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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
				return returnResultData(e,ResultEnum.Code.LOGIC_ERROR,e.getMessage());
			} else if (e instanceof AuthorizeException) {
				return returnResultData(e,ResultEnum.Code.IS_NOT_LOGIN,e.getMessage());
			} else if (e instanceof NoPermissionException) {
				return returnResultData(e,ResultEnum.Code.NO_PERMISSION,e.getMessage());
			} else if (e instanceof RpcException) {
				return returnResultData(e,ResultEnum.Code.SYSTEM_ERROR,"程序可能开小差了,对此我们深表抱歉!(错误代码:RpcException)");
			} else {
				e.printStackTrace();
				return returnResultData(e,ResultEnum.Code.SYSTEM_ERROR,"程序可能开小差了,对此我们深表抱歉!(错误代码:Unknown)");
			}
		}else if (responseType.equals(ResponseType.VIEW)) {
			if (e instanceof AuthorizeException) {
				return returnView(returnType,"redirect:/notLogin");
			} else if (e instanceof NoPermissionException) {
				return returnView(returnType,"redirect:/noPermission");
			} else if (e instanceof BusinessException) {
				return returnView(returnType,"redirect:/500");
			} else {
				e.printStackTrace();
				return "redirect:/500";
			}
		} else {
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	private ResultData returnResultData(Throwable e, ResultEnum.Code code, String message ){
		ResultData resultData = new ResultData();
		resultData.setStatus(code.getKey());
		resultData.setExceptionMessage(e.getMessage());
		resultData.setMessage(message);
		return resultData;
	}
	
	private Object returnView(Class<?> returnType,String viewUrl){
		if(returnType.getName().equals(String.class.getName())){
			return viewUrl;
		}else if(returnType.getName().equals(ModelAndView.class.getName())){
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName(viewUrl);
			return modelAndView;
		}
		return viewUrl;
	}
}
