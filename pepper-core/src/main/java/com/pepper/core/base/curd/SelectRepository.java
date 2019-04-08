package com.pepper.core.base.curd;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.pepper.core.Pager;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 */
public interface SelectRepository<T> {

	/**
	 * 使用jpql语句查询数据
	 * 
	 * @param jpql
	 *            jpql查询语句
	 * @return T
	 */
	@Transactional(readOnly=true)
	List<T> find(final String jpql);

	/**
	 * 使用jpql语句查询数据返回单个对象
	 * 
	 * @param jpql
	 *            jpql查询语句
	 * @return T
	 */
	@Transactional(readOnly=true)
	T findOne(final String jpql);

	/**
	 * 使用jpql语句查询数据返回Map对象
	 * 
	 * @param jpql
	 *            jpql查询语句
	 * @return T
	 */
	@Transactional(readOnly=true)
	List<Map<String, Object>> findToMap(final String jpql);

	/**
	 * 使用jpql语句查询数据返回单个Map对象
	 * 
	 * @param jpql
	 *            jpql查询语句
	 * @return T
	 */
	@Transactional(readOnly=true)
	Map<String, Object> findOneToMap(final String jpql);

	/**
	 * 使用jpql语句查询
	 * 
	 * @param jpql
	 *            jpql语句
	 * @return T
	 */
	@Transactional(readOnly=true)
	List<T> find(final String jpql,final Map<String, Object> searchParameter);

	/**
	 * 使用jpql语句查询
	 * 
	 * @param jpql
	 *            jpql语句
	 * @return T
	 */
	@Transactional(readOnly=true)
	List<Map<String, Object>> findToMap(final String jpql, final Map<String, Object> searchParameter);

	/**
	 * 使用jpql语句查询返回单个Map对象
	 * 
	 * @param jpql
	 *            jpql语句
	 * @return T
	 */
	@Transactional(readOnly=true)
	Map<String, Object> findOneToMap(final String jpql, final Map<String, Object> searchParameter);
	
	/**
	 * 分页查询(Pager中的searchParameter&sortParameter将不生效)
	 * @param jpql
	 * @return
	 */
	@Transactional(readOnly=true)
	Pager<T> findNavigator(final Pager<T> pager,final String jpql);
	
	/**
	 * 分页查询(Pager中的searchParameter&sortParameter将不生效)
	 * @param jpql
	 * @param parameter
	 * @return
	 */
	@Transactional(readOnly=true)
	Pager<T> findNavigator(final Pager<T> pager,final String jpql, final Map<String, Object> searchParameter);
	
	
	/**
	 * 分页查询
	 * @param pager
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly=true)
	Pager<T> findNavigator(final Pager<T> pager);
	
	/**
	 * 分页查询(Pager中的searchParameter&sortParameter将不生效)
	 * @param pager
	 * @param jpql
	 * @return
	 */
	@Transactional(readOnly=true)
	Pager<Map<String, Object>> findNavigatorToMap(final Pager<Map<String, Object>> pager,final String jpql);
	
	/**
	 * 分页查询(Pager中的searchParameter&sortParameter将不生效)
	 * @param pager
	 * @param jpql
	 * @param parameter
	 * @return
	 */
	@Transactional(readOnly=true)
	Pager<Map<String, Object>> findNavigatorToMap(final Pager<Map<String, Object>> pager,final String jpql,final Map<String, Object> searchParameter);
	
	/**
	 * 
	 * @param searchParameter
	 * @return
	 */
	@Transactional(readOnly=true)
	List<T> findAll(final Map<String, Object> searchParameter);
	
	/**
	 * 
	 * @param searchParameter
	 * @param sortParameter
	 * @return
	 */
	@Transactional(readOnly=true)
	List<T> findAll(final Map<String, Object> searchParameter,final Map<String, Object> sortParameter);

}
