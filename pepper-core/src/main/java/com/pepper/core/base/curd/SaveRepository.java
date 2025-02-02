package com.pepper.core.base.curd;

import java.util.Map;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 */
public interface SaveRepository<T> {


	/**
	 * 使用jpql新增数据
	 * @param jpql	jpql插入语句
	 * @return				int
	 */
	int save(final String jpql);
	
	/**
	 * 使用jpql新增数据
	 * @param jpql	jpql插入语句
	 * @param parameter			插入数据
	 * @return				int
	 */
	int save(final String jpql,final Map<String, Object> parameter);
		
}
