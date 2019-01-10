package com.pepper.core.base.curd.impl;

import java.util.Map;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pepper.core.base.curd.DeleteRepository;
import com.pepper.core.base.curd.RepositoryParameter;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 * @param <T>
 */
@Repository
public class DeleteRepositoryImpl<T>  implements DeleteRepository<T> {
	
	
	@Resource
	private EntityManager entityManager;

	@SuppressWarnings("unused")
	private Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int delete(String jpql) {
		return delete(jpql, null);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int delete(String jpql, Map<String, Object> parameter) {
		Query query = entityManager.createQuery(jpql);
		RepositoryParameter.setParameter(query, parameter);
		return query.executeUpdate(); 
	}
	
	

}
