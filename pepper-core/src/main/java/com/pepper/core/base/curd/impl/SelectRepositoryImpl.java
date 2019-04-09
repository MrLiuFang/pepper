package com.pepper.core.base.curd.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.pepper.core.Pager;
import com.pepper.core.base.curd.PredicateBuilder;
import com.pepper.core.base.curd.RepositoryParameter;
import com.pepper.core.base.curd.SelectRepository;
import com.pepper.core.base.curd.SortBuilder;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 * @param <T>
 */
public class SelectRepositoryImpl<T> implements SelectRepository<T> {
	
	private EntityManager entityManager;

	private Class<T> clazz;
	
	private SimpleJpaRepository<T, Serializable> simpleJpaRepository;

	public SelectRepositoryImpl(EntityManager entityManager, Class<T> clazz ,SimpleJpaRepository<T, Serializable> simpleJpaRepository) {
		super();
		this.entityManager = entityManager;
		this.clazz = clazz;
		this.simpleJpaRepository = simpleJpaRepository;
	}

	@Override
	public List<T> find(final String jpql) {
		TypedQuery<T> query = entityManager.createQuery(jpql,clazz);
		return query.getResultList();
	}

	@Override
	public T findOne(final String jpql) {
		List<T> list = find(jpql);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findToMap(final String jpql) {
		Query query = entityManager.createQuery(jpql);
		return query.getResultList();
	}

	@Override
	public Map<String, Object> findOneToMap(final String jpql) {
		List<Map<String,Object>> list = findToMap(jpql);
		if(list != null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(final String jpql,final Map<String, Object> searchParameter) {
		Query query = entityManager.createQuery(jpql);
		RepositoryParameter.setParameter(query, searchParameter);
		return query.getResultList();
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Map<String, Object>> findToMap(final String jpql,final Map<String, Object> parameter) {
		Query query = entityManager.createQuery(jpql);
		RepositoryParameter.setParameter(query, parameter);
		return query.getResultList();
	}

	@Override
	public Map<String, Object> findOneToMap(final String jpql,final Map<String, Object> parameter) {
		List<Map<String, Object>> list = findToMap(jpql,parameter);
		if(list !=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public Pager<T> findNavigator(final Pager<T> pager, final String jpql) {
		return findNavigator(pager, jpql, null);
	}

	@Override
	public Pager<T> findNavigator(final Pager<T> pager,final String jpql,final Map<String, Object> parameter) {
		pager.setData(executejpql(pager, jpql, parameter));
		pager.setTotalRow(getCount(jpql, parameter));
		return pager;
	}

	@Override
	public Pager<T> findNavigator(final Pager<T> pager) {
		Page<T> page = findAll(pager.getPageSize(),pager.getPageNo(),pager.getJpqlParameter().getSearchParameter(),pager.getJpqlParameter().getSortParameter());
		pageConvertPager(page,pager);
		return pager;
	}

	@Override
	public Pager<Map<String, Object>> findNavigatorToMap(final Pager<Map<String, Object>> pager, final String jpql) {
		return findNavigatorToMap(pager, jpql, null);
	}

	@Override
	public Pager<Map<String, Object>> findNavigatorToMap(final Pager<Map<String, Object>> pager, final String jpql,final Map<String, Object> parameter) {
		pager.setData(executejpql(pager, jpql, parameter));
		pager.setTotalRow(getCount(jpql, parameter));
		return pager;
	}
	
	/**
	 * 分页查询
	 * @param pager
	 * @param jpql
	 * @param parameter
	 * @return
	 */
	private List<?> executejpql(final Pager<?> pager, final String jpql, final Map<String, Object> parameter){
		Query query = entityManager.createQuery(jpql);
		RepositoryParameter.setParameter(query, parameter);
		query.setFirstResult(pager.getPageNo()*pager.getPageSize());
		query.setMaxResults(pager.getPageSize());
		return query.getResultList();
	}
	
	/**
	 * 查询总行数
	 * @param jpql
	 * @param parameter
	 * @return
	 */
	private Long getCount(final String jpql,final Map<String, Object> parameter){
		StringBuffer countjpql = new StringBuffer();
		countjpql.append(" select count(1) from ( ");
		countjpql.append( jpql );
		countjpql.append(" ) tb ");
		Query query = entityManager.createQuery(countjpql.toString());
		RepositoryParameter.setParameter(query, parameter);
		return (Long) query.getSingleResult();
	}

	/**
	 * 
	 * @param page
	 * @param pager
	 * @return
	 */
	private Pager<T> pageConvertPager(final Page<T> page,final Pager<T> pager){
		pager.setResults(page.getContent());
		pager.setTotalRow(Long.valueOf(page.getTotalElements()));
		return pager;
	}

	@Override
	public List<T> findAll(final Map<String, Object> searchParameter) {
		return findAll(searchParameter, null);
	}

	@Override
	public List<T> findAll(final Map<String, Object> searchParameter, final Map<String, Object> sortParameter) {
		return findAll(Integer.MAX_VALUE,1,searchParameter,sortParameter).getContent();
	}
	
	private Page<T> findAll(final Integer pageSize,final Integer pageNo,final Map<String, Object> searchParameter,final Map<String, Object> sortParameter) {
		Specification<T> specification = new Specification<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				//动态构建where条件
				List<Predicate> list = PredicateBuilder.builder(root, criteriaBuilder,searchParameter);
				return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
			}
		};
		//构建排序
		Sort sort = SortBuilder.builder(sortParameter);
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize,sort);
		Page<T> page = simpleJpaRepository.findAll(specification,pageable);
		return page;
	}
	
	
	
}
