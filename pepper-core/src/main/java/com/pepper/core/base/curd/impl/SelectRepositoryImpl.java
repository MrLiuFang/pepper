package com.pepper.core.base.curd.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.param.NamedParameterSpecification;
import org.hibernate.param.ParameterSpecification;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.pepper.core.IEnum;
import com.pepper.core.Pager;
import com.pepper.core.base.curd.PredicateBuilder;
import com.pepper.core.base.curd.RepositoryParameter;
import com.pepper.core.base.curd.SelectRepository;
import com.pepper.core.base.curd.SortBuilder;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * 
 * @author mrliu
 *
 * @param <T>
 */
public class SelectRepositoryImpl<T> implements SelectRepository<T> {

	private EntityManager entityManager;

	private Class<T> clazz;

	private SimpleJpaRepository<T, Serializable> simpleJpaRepository;

	public SelectRepositoryImpl(EntityManager entityManager, Class<T> clazz,
			SimpleJpaRepository<T, Serializable> simpleJpaRepository) {
		super();
		this.entityManager = entityManager;
		this.clazz = clazz;
		this.simpleJpaRepository = simpleJpaRepository;
	}

	@Override
	public List<T> find(final String jpql) {
		TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
		return query.getResultList();
	}

	@Override
	public T findOne(final String jpql) {
		Pager<T> pager = new Pager<T>();
		pager.setPageNo(1);
		pager.setPageSize(1);
		pager = findNavigator(pager ,jpql);
		List<T> list = pager.getResults();
		if (list != null && list.size() > 0) {
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
		List<Map<String, Object>> list = findToMap(jpql);
		if (list != null && list.size() > 0) {
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
	public List<Map<String, Object>> findToMap(final String jpql, final Map<String, Object> searchParameter) {
		Query query = entityManager.createQuery(jpql);
		RepositoryParameter.setParameter(query, searchParameter);
		return query.getResultList();
	}

	@Override
	public Map<String, Object> findOneToMap(final String jpql, final Map<String, Object> searchParameter) {
		List<Map<String, Object>> list = findToMap(jpql, searchParameter);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new HashMap<String,Object>();
	}

	@Override
	public Pager<T> findNavigator(final Pager<T> pager, final String jpql) {
		return findNavigator(pager, jpql, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager<T> findNavigator(final Pager<T> pager, final String jpql, final Map<String, Object> searchParameter) {
		List<T> list = (List<T>) executeJpql(pager, jpql, searchParameter);
		pager.setResults(list);
		pager.setTotalRow(getCount(jpql, searchParameter));
		return pager;
	}

	@Override
	public Pager<T> findNavigator(final Pager<T> pager) {
		Page<T> page = findAll(pager.getPageSize(), pager.getPageNo(), pager.getJpqlParameter().getSearchParameter(),
				pager.getJpqlParameter().getSortParameter());
		pageConvertPager(page, pager);
		return pager;
	}

	@Override
	public Pager<Map<String, Object>> findNavigatorToMap(final Pager<Map<String, Object>> pager, final String jpql) {
		return findNavigatorToMap(pager, jpql, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pager<Map<String, Object>> findNavigatorToMap(final Pager<Map<String, Object>> pager, final String jpql,
			final Map<String, Object> searchParameter) {
		List<Map<String, Object>> list =  (List<Map<String, Object>>) executeJpql(pager, jpql, searchParameter);
		pager.setResults(list);
		pager.setTotalRow(getCount(jpql, searchParameter));
		return pager;
	}

	/**
	 * 分页查询
	 * 
	 * @param pager
	 * @param jpql
	 * @param parameter
	 * @return
	 */
	private List<?> executeJpql(final Pager<?> pager, final String jpql, final Map<String, Object> searchParameter) {
		Query query = entityManager.createQuery(jpql);
		RepositoryParameter.setParameter(query, searchParameter);
		query.setFirstResult((pager.getPageNo()-1) * pager.getPageSize());
		query.setMaxResults(pager.getPageSize());
		return query.getResultList();
	}

	/**
	 * 查询总行数
	 * 
	 * @param jpql
	 * @param parameter
	 * @return
	 */
	private Long getCount(final String jpql, final Map<String, Object> searchParameter) {
		SessionFactoryImplementor sessionFactoryImplementor = (SessionFactoryImplementor)entityManager.unwrap(Session.class).getSessionFactory();
		QueryTranslatorImpl queryTranslator=new QueryTranslatorImpl(jpql,jpql,searchParameter==null?Collections.EMPTY_MAP:searchParameter,sessionFactoryImplementor);
		queryTranslator.compile(searchParameter==null?Collections.EMPTY_MAP:searchParameter, true);
		String sql = queryTranslator.getSQLString();
		List<ParameterSpecification> parameter = queryTranslator.getCollectedParameterSpecifications();
		StringBuffer countSql = new StringBuffer();
		countSql.append(" select count(1) from (");
		countSql.append(sql);
		countSql.append(") tb");
		Query query = entityManager.createNativeQuery(countSql.toString());
		for(int i =1; i<=parameter.size(); i++){
			Object object = searchParameter.get( ((NamedParameterSpecification)parameter.get(i-1)).getName());
			if(object.getClass().isEnum() ) {
				query.setParameter(i, ((IEnum)object).getKey());
			}else {
				query.setParameter(i, object);
			}
			
		}
		return Long.valueOf(query.getSingleResult().toString());
	}

	/**
	 * 
	 * @param page
	 * @param pager
	 * @return
	 */
	private Pager<T> pageConvertPager(final Page<T> page, final Pager<T> pager) {
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
		return findAll(Integer.MAX_VALUE, 1, searchParameter, sortParameter).getContent();
	}

	private Page<T> findAll(final Integer pageSize, final Integer pageNo, final Map<String, Object> searchParameter,
			final Map<String, Object> sortParameter) {
		Specification<T> specification = new Specification<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				// 动态构建where条件
				List<Predicate> list = PredicateBuilder.builder(root, criteriaBuilder, searchParameter);
				return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
			}
		};
		// 构建排序
		Sort sort = SortBuilder.builder(sortParameter);
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		Page<T> page = simpleJpaRepository.findAll(specification, pageable);
		return page;
	}

}
