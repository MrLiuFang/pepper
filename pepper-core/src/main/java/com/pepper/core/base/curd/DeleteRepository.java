package com.pepper.core.base.curd;

import java.util.Map;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 */
public interface DeleteRepository<T> {
	
	/**
	 * 根据jpql语句删除
	 * @param jpql	jpql删除语句
	 * @return				int
	 */
	int delete(final String jpql);

	/**
	 * 根据jpql语句删除
	 * @param jpql	jpql删除语句
	 * @param parameter			删除条件
	 * @return				int
	 */
	int delete(final String jpql, final Map<String, Object> parameter);


}
