package com.pepper.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pepper.core.ResultData;
import com.pepper.core.ResultEnum;

/**
 * 业务异常返回信息处理类
 *
 * @author mrliu
 *
 */
public class BusinessExceptionData {

	private static Logger logger = LoggerFactory.getLogger(BusinessExceptionData.class);

	public static synchronized Object getResultData(Throwable e, Class<?> returnType) {
		logger.info(e.getMessage());
		if (returnType != null && returnType.getName().equals(String.class.getName())) {
			return "redirect:/500";
		} else {
			ResultData resultData = new ResultData();
			if (e instanceof BusinessException) {
				resultData.setStatus(ResultEnum.Code.LOGIC_ERROR.getKey());
				resultData.setMessage(e.getMessage());
			}
			return resultData;
		}
	}
}
