package com.pepper.core.base.curd;

import java.util.Map;

import javax.persistence.Query;


/**
 * 
 * @author mrliu
 *
 */
public class RepositoryParameter {
	
	/**
	 * 设置参数
	 * @param query
	 * @param para
	 */
	public synchronized static void setParameter(final Query query,final Map<String, Object> parameter) {
		if(parameter==null || query==null){
			return;
		}
		for (String key : parameter.keySet()) {
			query.setParameter(key, parameter.get(key));
		}
	}
	
	

}
