package com.pepper.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.pepper.core.constant.GlobalConstant;

/**
 * 分页返回数据信息
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Pager<T> extends ResultData implements Serializable {
	private static final long serialVersionUID = -2185444016922016927L;
	private long totalRow;
	private int pageSize = 20;
	private int pageNo = 1;
	private List<T> results = new ArrayList<T>();
	
	@JsonIgnore
	private JpqlParameter jpqlParameter;

	public List<T> getResults() {
		return results;
	}

	public Pager<T> setResults(final List<T> results) {
		this.results = results;
		return this;
	}

	public long getTotalRow() {
		return totalRow;
	}

	public Pager<T> setTotalRow(final Long totalRow) {
		this.totalRow = totalRow;
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	public Pager<T> setPageSize( final Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public int getPageNo() {
		return pageNo;
	}

	public Pager<T> setPageNo( final Integer pageNo) {
		this.pageNo = pageNo;
		return this;
	}

	@JsonIgnore
	public JpqlParameter getJpqlParameter() {
		return jpqlParameter;
	}

	public Pager() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if(requestAttributes == null){
			return;
		}
		HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
		String pageSize = request.getParameter(GlobalConstant.PAGE_SIZE);
		if (NumberUtils.isDigits(request.getParameter(GlobalConstant.PAGE_SIZE))) {
			this.setPageSize(Integer.valueOf(pageSize));
		}else{
			this.setPageSize(30);
		}
		String pageNo = request.getParameter(GlobalConstant.PAGE_NO);
		if (NumberUtils.isDigits(pageNo)) {
			this.setPageNo(Integer.valueOf(pageNo));
		}else{
			this.setPageNo(1);
		}
		jpqlParameter = new JpqlParameter();
	}
	
	
	

}
