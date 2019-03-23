package com.pepper.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.pepper.core.ResultEnum.Code;
import com.pepper.util.BeanToMapUtil;

/**
 * 返回给客户端的结果集
 *
 * @author mrliu
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ResultData implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 981792735336739260L;


	/**
	 * 消息
	 */
	protected String message = "操作成功";

	/**
	 * 异常信息
	 */
	protected String exceptionMessage;

	/**
	 * 状态编码
	 */
	private Integer status = Code.SUCCESS.getKey();

	/**
	 * 调转URL
	 */
	private String loadUrl;
	
	private Integer code = 200;

	/**
	 * 结果集
	 */
	private Map<String, Object> data = new HashMap<String, Object>();

	public Map<String, Object> getData() {
		return data;
	}

	public ResultData setData(Map<String, Object> data) {
		this.data.putAll(data);
		return this;
	}

	/**
	 * @param javaBean
	 */
	public ResultData setData(Object javaBean) {
		if (javaBean != null) {
			BeanToMapUtil.transBeanToMap(data, javaBean);
		}
		return this;
	}

	public ResultData setData(String key, Object object) {
		data.put(key, object);
		return this;
	}

	public ResultData setData(String key, Collection<Object> collection) {
		data.put(key, collection);
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ResultData setMessage(String message) {
		this.message = message;
		return this;
	}

	public Integer getStatus() {
		return status;
	}

	public ResultData setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public ResultData setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
		return this;
	}

	public String getLoadUrl() {
		return loadUrl;
	}

	public ResultData setLoadUrl(String loadUrl) {
		this.loadUrl = loadUrl;
		return this;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	
}
