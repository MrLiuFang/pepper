package com.pepper.core.exception;

import com.pepper.core.ResultData;
import com.pepper.core.ResultEnum;

public class NoPermissionExceptionData {

	public static synchronized Object getResultData(Throwable e, Class<?> returnType) {
		if (returnType != null && returnType.getName().equals(String.class.getName())) {
			return "redirect:/noPermission";
		}else{
			ResultData resultData = new ResultData();
			if (e instanceof NoPermissionException) {
				resultData.setStatus(ResultEnum.Code.NO_PERMISSION.getKey());
				resultData.setMessage(e.getMessage());
			}
			return resultData;
		}
	}
}
