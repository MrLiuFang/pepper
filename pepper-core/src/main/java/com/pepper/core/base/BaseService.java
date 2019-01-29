package com.pepper.core.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.pepper.core.Pager;

/**
 * 
 * @author mrliu
 *
 * @param <T>
 */
public interface BaseService<T>{

	public List<T> findAll();

	public List<T> findAll(Sort sort);

	public List<T> findAllById(Iterable<Serializable> ids);

	public <S extends T> List<S> saveAll(Iterable<S> entities);

	public <S extends T> S saveAndFlush(S entity);
	
	public void update(T entity);

	public void deleteInBatch(Iterable<T> entities);

	public void deleteAllInBatch();

	public T getOne(Serializable id);

	public <S extends T> List<S> findAll(Example<S> example);

	public <S extends T> List<S> findAll(Example<S> example, Sort sort);

	public Page<T> findAll(Pageable pageable);

	public <S extends T> S save(S entity);

	public Optional<T> findById(Serializable id);

	public boolean existsById(Serializable id);

	public long count();
	
	public void deleteById(Serializable id);

	public void delete(T entity);

	public void deleteAll(Iterable<? extends T> entities);

	public void deleteAll();

	public <S extends T> Optional<S> findOne(Example<S> example);

	public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);
	
	List<T> findAll(Map<String, Object> searchParameter);
	
	List<T> findAll(Map<String, Object> searchParameter,Map<String, Object> sortParameter);

	public <S extends T> long count(Example<S> example);

	public <S extends T> boolean exists(Example<S> example);
	
	/**
	 * 分页查询
	 * @param pager
	 * @param entity
	 * @return
	 */
	Pager<T> findNavigator(Pager<T> pager);
	
	
}
