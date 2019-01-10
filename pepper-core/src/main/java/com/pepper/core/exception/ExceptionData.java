package com.pepper.core.exception;

import com.alibaba.dubbo.rpc.RpcException;
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
	public Object getResultData(Throwable e, Class<?> returnType) {
		if (returnType != null && (returnType.getName().equals(ResultData.class.getName())
				|| "java.lang.Object".equals(returnType.getName()))) {
			ResultData resultData = new ResultData();
			if (e instanceof BusinessException) {
				return BusinessExceptionData.getResultData(e, null);
			} else if (e instanceof AuthorizeException) {
				return AuthorizeExceptionData.getResultData(e, null);
			} else if (e instanceof NoPermissionException) {
				return NoPermissionExceptionData.getResultData(e, null);
			} else if (e instanceof RpcException) {
				resultData.setStatus(ResultEnum.Code.SYSTEM_ERROR.getKey());
				resultData.setExceptionMessage(e.getMessage());
				resultData.setMessage("程序可能开小差了,对此我们深表抱歉!(错误代码:RpcException)");
			} else {
				e.printStackTrace();
				resultData.setStatus(ResultEnum.Code.SYSTEM_ERROR.getKey());
				resultData.setExceptionMessage(e.getMessage());
				resultData.setMessage("程序可能开小差了,对此我们深表抱歉!(错误代码:Unknown)");
			}
			return resultData;
		} else if (returnType != null && returnType.getName().equals(String.class.getName())) {
			if (e instanceof AuthorizeException) {
				return AuthorizeExceptionData.getResultData(e, returnType);
			} else if (e instanceof NoPermissionException) {
				return NoPermissionExceptionData.getResultData(e, returnType);
			} else if (e instanceof BusinessException) {
				return BusinessExceptionData.getResultData(e, returnType);
			} else {
				e.printStackTrace();
				return "redirect:/500";
			}
		} else {
			throw new RuntimeException(e.getMessage(),e);
		}
	}
}
