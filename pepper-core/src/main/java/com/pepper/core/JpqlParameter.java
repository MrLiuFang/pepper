package com.pepper.core;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pepper.core.constant.GlobalConstant;

/**
 * 构建查询参数
 * @author mrliu
 *
 */
public class JpqlParameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3482359007036882853L;

	@JsonIgnore
	private Map<String, Object> searchParameter = new HashMap<String, Object>();
	
	@JsonIgnore
	private Map<String, Object> sortParameter = new HashMap<String, Object>();
		
	public JpqlParameter() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if(requestAttributes == null){
			return;
		}
		HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
		setJpqlParameter(request,GlobalConstant.JPQL_PREFIX_SEARCH,searchParameter);
		setJpqlParameter(request,GlobalConstant.JPQL_PREFIX_SORT,sortParameter);
	}
	
	private void setJpqlParameter(HttpServletRequest request, String prefix, Map<String, Object> jpqlParameter) {
		Enumeration<String> paramNames = request.getAttributeNames();
		while (paramNames!=null && paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			if (paramName.startsWith(prefix)) {
				String ignoreFixName = paramName.substring(prefix.length());
				Object attVal = request.getAttribute(paramName);
				String[] values = null;
				if (attVal instanceof String[]) {
					values = (String[]) attVal;
				} else if (attVal instanceof String) {
					values = new String[] { (String) attVal };
				}
				if (values!=null && values.length>0) {
					if (values.length > 1) {
//						jpqlParameter.put(ignoreFixName, values);
					} else {
						jpqlParameter.put(ignoreFixName, values[0]);
					}
				}
			}
		}
		paramNames = request.getParameterNames();
		while (paramNames!=null && paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			if (paramName.startsWith(prefix)) {
				String ignoreFixName = paramName.substring(prefix.length());
				String[] attVal = request.getParameterValues(paramName);
				if (attVal!=null && attVal.length>0) {
					if (attVal.length > 1) {
//						jpqlParameter.put(ignoreFixName, attVal);
					} else {
						jpqlParameter.put(ignoreFixName, attVal[0].trim());
					}
				}
			}
		}
	}
	
	@JsonIgnore
	public Map<String, Object> getSearchParameter() {
		return searchParameter;
	}
	
	@JsonIgnore
	public Map<String, Object> getSortParameter() {
		return sortParameter;
	}

	public void setSearchParameter(Map<String, Object> searchParameter) {
		this.searchParameter = searchParameter;
	}

	public void setSortParameter(Map<String, Object> sortParameter) {
		this.sortParameter = sortParameter;
	}
	
	public void setSearchParameter(String key, Object value) {
		this.searchParameter.put(key, value);
	}

	public void setSortParameter(String key, Object value) {
		this.sortParameter.put(key, value);
	}
	
	
}
