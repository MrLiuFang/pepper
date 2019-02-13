package com.pepper.core.base.curd.impl;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.pepper.core.base.curd.RepositoryParameter;
import com.pepper.core.base.curd.UpdateRepository;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 * @param <T>
 */
public class UpdateRepositoryImpl<T>  implements UpdateRepository<T> {

	private EntityManager entityManager;
	
	private JpaEntityInformation<T, Serializable> entityInformation;
	
	private SimpleJpaRepository<T, Serializable> simpleJpaRepository;

	public UpdateRepositoryImpl(EntityManager entityManager, JpaEntityInformation<T, Serializable> entityInformation, SimpleJpaRepository<T, Serializable> simpleJpaRepository) {
		super();
		this.entityManager = entityManager;
		this.entityInformation = entityInformation;
		this.simpleJpaRepository = simpleJpaRepository;
	}

	private Session getSession() {
		return entityManager.unwrap(Session.class);
	}
	
	@Override
	public int update(final String jpql) {
		return update(jpql, null);
	}

	@Override
	public int update(final String jpql,final Map<String, Object> parameter) {
		Query query = entityManager.createQuery(jpql);
		RepositoryParameter.setParameter(query, parameter);
		return query.executeUpdate(); 
	}

	@Override
	public void update(final T entity) {
		Serializable id = entityInformation.getId(entity);
		T oldEntity = simpleJpaRepository.findById(id).get();
		if(oldEntity == null){
			return;
		}
		BeanUtil.copyProperties(entity,oldEntity,CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
		getSession().update(oldEntity);
	}

}
