package com.pepper.core.base.curd;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pepper.core.Pager;
import com.pepper.core.base.BaseDao;
import com.pepper.core.base.curd.impl.DeleteRepositoryImpl;
import com.pepper.core.base.curd.impl.SaveRepositoryImpl;
import com.pepper.core.base.curd.impl.SelectRepositoryImpl;
import com.pepper.core.base.curd.impl.UpdateRepositoryImpl;

/**
 * 
 * @author mrliu
 *
 */
@NoRepositoryBean

public abstract class PepperSimpleJpaRepository<T> extends SimpleJpaRepository<T, Serializable> implements BaseDao<T> {

	private SelectRepository<T> selectRepository;

	private UpdateRepository<T> updateRepository;

	private SaveRepository<T> saveRepository;

	private DeleteRepository<T> deleteRepository;

	protected EntityManager entityManager;

	protected JpaEntityInformation<T, Serializable> entityInformation;

	// T的具体类
	protected Class<T> clazz;

	public PepperSimpleJpaRepository(JpaEntityInformation<T, Serializable> entityInformation,
			EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
		this.entityInformation = entityInformation;
		this.clazz = entityInformation.getJavaType();
		updateRepository = new UpdateRepositoryImpl<T>(entityManager, entityInformation, this);
		selectRepository = new SelectRepositoryImpl<T>(entityManager, clazz, this);
		saveRepository = new SaveRepositoryImpl<T>(entityManager);
		deleteRepository = new DeleteRepositoryImpl<T>(entityManager);
	}

	

	protected Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	@Override
	@Transactional(readOnly=true)
	public List<T> findAll(Map<String, Object> searchParameter) {
		return selectRepository.findAll(searchParameter);
	}

	@Override
	@Transactional(readOnly=true)
	public List<T> findAll(Map<String, Object> searchParameter, Map<String, Object> sortParameter) {
		return selectRepository.findAll(searchParameter, sortParameter);
	}

	@Override
	@Transactional(readOnly=true)
	public List<T> find(String jpql) {
		return selectRepository.find(jpql);
	}

	@Override
	@Transactional(readOnly=true)
	public T findOne(String jpql) {
		return selectRepository.findOne(jpql);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Map<String, Object>> findToMap(String jpql) {
		return selectRepository.findToMap(jpql);
	}

	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> findOneToMap(String jpql) {
		return selectRepository.findOneToMap(jpql);
	}

	@Override
	@Transactional(readOnly=true)
	public List<T> find(String jpql, Map<String, Object> parameter) {
		return selectRepository.find(jpql, parameter);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Map<String, Object>> findToMap(String jpql, Map<String, Object> parameter) {
		return selectRepository.findToMap(jpql, parameter);
	}

	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> findOneToMap(String jpql, Map<String, Object> parameter) {
		return selectRepository.findOneToMap(jpql, parameter);
	}

	@Override
	@Transactional(readOnly=true)
	public Pager<T> findNavigator(Pager<T> pager, String jpql) {
		return selectRepository.findNavigator(pager, jpql);
	}

	@Override
	@Transactional(readOnly=true)
	public Pager<T> findNavigator(Pager<T> pager, String jpql, Map<String, Object> parameter) {
		return selectRepository.findNavigator(pager, jpql, parameter);
	}

	@Override
	@Transactional(readOnly=true)
	public Pager<T> findNavigator(Pager<T> pager) {
		return selectRepository.findNavigator(pager);
	}

	@Override
	@Transactional(readOnly=true)
	public Pager<Map<String, Object>> findNavigatorToMap(Pager<Map<String, Object>> pager, String jpql) {
		return selectRepository.findNavigatorToMap(pager, jpql);
	}

	@Override
	@Transactional(readOnly=true)
	public Pager<Map<String, Object>> findNavigatorToMap(Pager<Map<String, Object>> pager, String jpql,
			Map<String, Object> parameter) {
		return selectRepository.findNavigatorToMap(pager, jpql, parameter);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int update(String jpql) {
		return updateRepository.update(jpql);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int update(String jpql, Map<String, Object> parameter) {
		return updateRepository.update(jpql, parameter);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void update(T entity) {
		updateRepository.update(entity);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int save(String jpql) {
		return saveRepository.save(jpql);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int save(String jpql, Map<String, Object> parameter) {
		return saveRepository.save(jpql, parameter);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int delete(String jpql) {
		return deleteRepository.delete(jpql);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int delete(String jpql, Map<String, Object> parameter) {
		return deleteRepository.delete(jpql, parameter);
	}
}
