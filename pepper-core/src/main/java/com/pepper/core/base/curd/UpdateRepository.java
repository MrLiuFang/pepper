package com.pepper.core.base.curd;

import java.util.Map;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 */
public interface UpdateRepository<T> {
	
	/**
	 * 使用jpql语句更新数据
	 * @param jpql	jpql更新语句
	 */
	int update(final String jpql);

	/**
	 * 使用jpql语句更新数据
	 * @param jpql	jpql更新语句
	 * @param parameter			跟新条件
	 */
	int update(final String jpql,final Map<String, Object> parameter);

	/**
	 * 根据model对象更新数据
	 * @param entity		model对象<T>
	 */
	void update(final T entity);

}
