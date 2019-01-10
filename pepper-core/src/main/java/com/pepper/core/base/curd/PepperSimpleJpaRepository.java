package com.pepper.core.base.curd;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.pepper.core.Pager;
import com.pepper.core.base.BaseDao;

/**
 * 
 * @author mrliu
 *
 */
@NoRepositoryBean
public abstract class PepperSimpleJpaRepository<T> extends SimpleJpaRepository<T, Serializable>	implements BaseDao<T> {
	
	@Resource
	private SelectRepository<T> selectRepository;
	
	@Resource
	private UpdateRepository<T> updateRepository;
	
	@Resource
	private SaveRepository<T> saveRepository;
	
	@Resource
	private DeleteRepository<T> deleteRepository;
	
	@Resource
	protected EntityManager entityManager;

	protected JpaEntityInformation<T, Serializable> entityInformation;
	
	// T的具体类
	protected Class<T> clazz;
	
	public PepperSimpleJpaRepository(JpaEntityInformation<T, Serializable> entityInformation, EntityManager entityManager){
		super(entityInformation, entityManager);
		this.entityInformation = entityInformation;
		this.clazz = entityInformation.getJavaType();		
	}
	
	protected Session getSession() {
		return entityManager.unwrap(Session.class);
	}
	
	
	
	
	@Override
	public List<T> findAll(Map<String, Object> searchParameter) {
		return selectRepository.findAll(searchParameter);
	}

	@Override
	public List<T> findAll(Map<String, Object> searchParameter, Map<String, Object> sortParameter) {
		return selectRepository.findAll(searchParameter, sortParameter);
	}

	@Override
	public List<T> find(String jpql) {
		return selectRepository.find(jpql);
	}

	@Override
	public T findOne(String jpql) {
		return selectRepository.findOne(jpql);
	}

	@Override
	public List<Map<String, Object>> findToMap(String jpql) {
		return selectRepository.findToMap(jpql);
	}

	@Override
	public Map<String, Object> findOneToMap(String jpql) {
		return selectRepository.findOneToMap(jpql);
	}

	@Override
	public List<T> find(String jpql, Map<String, Object> parameter) {
		return selectRepository.find(jpql, parameter);
	}

	@Override
	public List<Map<String, Object>> findToMap(String jpql, Map<String, Object> parameter) {
		return selectRepository.findToMap(jpql, parameter);
	}

	@Override
	public Map<String, Object> findOneToMap(String jpql, Map<String, Object> parameter) {
		return selectRepository.findOneToMap(jpql, parameter);
	}
	
	@Override
	public Pager<T> findNavigator(Pager<T> pager, String jpql) {
		return selectRepository.findNavigator(pager, jpql);
	}

	@Override
	public Pager<T> findNavigator(Pager<T> pager, String jpql, Map<String, Object> parameter) {
		return selectRepository.findNavigator(pager, jpql, parameter);
	}

	@Override
	public Pager<T> findNavigator(Pager<T> pager) {
		return selectRepository.findNavigator(pager);
	}

	@Override
	public Pager<Map<String, Object>> findNavigatorToMap(Pager<Map<String, Object>> pager, String jpql) {
		return selectRepository.findNavigatorToMap(pager, jpql);
	}

	@Override
	public Pager<Map<String, Object>> findNavigatorToMap(Pager<Map<String, Object>> pager, String jpql,
			Map<String, Object> parameter) {
		return selectRepository.findNavigatorToMap(pager, jpql, parameter);
	}

	@Override
	public int update(String jpql) {
		return updateRepository.update(jpql);
	}

	@Override
	public int update(String jpql, Map<String, Object> parameter) {
		return updateRepository.update(jpql, parameter);
	}

	@Override
	public void update(T entity) {
		updateRepository.update(entity);
	}
	

	@Override
	public int save(String jpql) {
		return saveRepository.save(jpql);
	}

	@Override
	public int save(String jpql, Map<String, Object> parameter) {
		return saveRepository.save(jpql, parameter);
	}

	@Override
	public int delete(String jpql) {
		return deleteRepository.delete(jpql);
	}

	@Override
	public int delete(String jpql, Map<String, Object> parameter) {
		return deleteRepository.delete(jpql, parameter);
	}
}
