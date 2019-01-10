package com.pepper.core.exception;

import com.pepper.core.ResultData;
import com.pepper.core.ResultEnum;

/**
 * 登录异常返回信息处理类
 *
 * @author mrliu
 *
 */
public class AuthorizeExceptionData {

	public static synchronized Object getResultData(Throwable e, Class<?> returnType) {
		if (returnType != null && returnType.getName().equals(String.class.getName())) {
			return "redirect:/notLogin";
		}else  {
			ResultData resultData = new ResultData();
			if (e instanceof AuthorizeException) {
				resultData.setStatus(ResultEnum.Code.IS_NOT_LOGIN.getKey());
				resultData.setMessage(e.getMessage());
			}
			return resultData;
		}
	}
}
