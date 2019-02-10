package com.pepper.core.base.curd.impl;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pepper.core.base.curd.RepositoryParameter;
import com.pepper.core.base.curd.UpdateRepository;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 * @param <T>
 */
public class UpdateRepositoryImpl<T>  implements UpdateRepository<T> {

	private EntityManager entityManager;

	public UpdateRepositoryImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	private Session getSession() {
		return entityManager.unwrap(Session.class);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int update(final String jpql) {
		return update(jpql, null);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int update(final String jpql,final Map<String, Object> parameter) {
		Query query = entityManager.createQuery(jpql);
		RepositoryParameter.setParameter(query, parameter);
		return query.executeUpdate(); 
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void update(final T entity) {
		getSession().update(entity);
	}

}
