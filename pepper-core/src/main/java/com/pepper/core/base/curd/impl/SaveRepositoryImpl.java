package com.pepper.core.base.curd.impl;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pepper.core.base.curd.RepositoryParameter;
import com.pepper.core.base.curd.SaveRepository;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 * @param <T>
 */
public class SaveRepositoryImpl<T>  implements SaveRepository<T> {

	private EntityManager entityManager;
	
	public SaveRepositoryImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int save(final String jpql,final Map<String, Object> parameter) {
		Query query = entityManager.createQuery(jpql);
		RepositoryParameter.setParameter(query, parameter);
		return query.executeUpdate(); 
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int save(final String jpql) {
		return save(jpql, null);
	}

}
