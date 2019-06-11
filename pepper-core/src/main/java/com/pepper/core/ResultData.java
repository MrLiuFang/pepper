package com.pepper.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.jaxb.SpringDataJaxb.PageRequestDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.pepper.core.ResultEnum.Status;
import com.pepper.util.BeanToMapUtil;

/**
 * 返回给客户端的结果集
 *
 * @author mrliu
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ResultData  implements Serializable {

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
	private Integer status = Status.SUCCESS.getKey();

	/**
	 * 调转URL
	 */
	private String loadUrl;
	

	/**
	 * 结果集
	 */
	private Map<String, Object> data = new HashMap<String, Object>();
	
	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data.putAll(data);
	}

	/**
	 * @param javaBean
	 */
	public void setData(Object javaBean) {
		if (javaBean != null) {
			BeanToMapUtil.transBeanToMap(data, javaBean);
		}
	}

	public void setData(String key, Object object) {
		data.put(key, object);
	}

	public void setData(String key, Collection<Object> collection) {
		data.put(key, collection);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getLoadUrl() {
		return loadUrl;
	}

	public void setLoadUrl(String loadUrl) {
		this.loadUrl = loadUrl;
	}
	
}
