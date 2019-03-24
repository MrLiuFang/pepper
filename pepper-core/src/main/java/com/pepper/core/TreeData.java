package com.pepper.core;

import com.pepper.core.ResultEnum.Status;

/**
 * 
 * @author mrliu
 *
 * @param <T>
 */
public class TreeData<T> {

	private Integer code = Status.SUCCESS.getKey();
	protected String msg = "操作成功";
	private Integer count;
	private T data;
	private Object extData;

	public Object getExtData() {
		return extData;
	}

	public void setExtData(Object extData) {
		this.extData = extData;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Object getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
