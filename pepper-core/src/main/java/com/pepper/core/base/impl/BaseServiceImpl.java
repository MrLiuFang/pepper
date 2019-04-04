package com.pepper.core.base.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ReflectionUtils;

import com.pepper.core.Pager;
import com.pepper.core.base.BaseDao;
import com.pepper.core.base.BaseModel;
import com.pepper.core.base.BaseService;
import com.pepper.core.base.ICurrentUser;

/**
 * 
 * @author mrliu
 *
 * @param <T>
 */
public abstract class BaseServiceImpl<T>	 implements BaseService<T> {


	@Autowired
	private BaseDao<T> baseDao;
	
	@Resource
	private ICurrentUser currentUser;

	@Override
	public List<T> findAll() {
		return baseDao.findAll();
	}

	@Override
	public List<T> findAll(Sort sort) {
		return baseDao.findAll(sort);
	}

	@Override
	public List<T> findAllById(Iterable<Serializable> ids) {
		return baseDao.findAllById(ids);
	}

	@Override
	public <S extends T> List<S> saveAll(Iterable<S> entities) {
		return baseDao.saveAll(entities);
	}

	@Override
	public <S extends T> S saveAndFlush(S entity) {
		return baseDao.saveAndFlush(entity);
	}

	@Override
	public void update(T entity) {
		setUpdateInfo(entity);
		baseDao.update(entity);
	}

	@Override
	public void deleteInBatch(Iterable<T> entities) {
		baseDao.deleteInBatch(entities);
	}

	@Override
	public void deleteAllInBatch() {
		baseDao.deleteAllInBatch();
	}

	@Override
	public T getOne(Serializable id) {
		return baseDao.getOne(id);
	}

	@Override
	public <S extends T> List<S> findAll(Example<S> example) {
		return baseDao.findAll(example);
	}

	@Override
	public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
		return baseDao.findAll(example, sort);
	}
	
	

	@Override
	public List<T> findAll(Map<String, Object> searchParameter) {
		return baseDao.findAll(searchParameter);
	}

	@Override
	public List<T> findAll(Map<String, Object> searchParameter, Map<String, Object> sortParameter) {
		return baseDao.findAll(searchParameter, sortParameter);
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		return baseDao.findAll(pageable);
	}

	@Override
	public <S extends T> S save(S entity) {
		setCreateInfo(entity);
		return baseDao.save(entity);
	}

	@Override
	public T findById(Serializable id) {
		try {
			if(existsById(id)) {
				Optional<T> optional =  baseDao.findById(id);
				if(optional.isPresent()) {
					return optional.get();
				}else {
					return null;
				}
			}else {
				return null;
			}
		}catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean existsById(Serializable id) {
		return baseDao.existsById(id);
	}

	@Override
	public long count() {
		return baseDao.count();
	}

	@Override
	public void deleteById(Serializable id) {
		try {
			if(existsById(id)) {
				baseDao.deleteById(id);
			}
		}catch (Exception e) {
		}
	}

	@Override
	public void delete(T entity) {
		baseDao.delete(entity);
	}

	@Override
	public void deleteAll(Iterable<? extends T> entities) {
		baseDao.deleteAll(entities);
	}

	@Override
	public void deleteAll() {
		baseDao.deleteAll();
	}

	@Override
	public <S extends T> Optional<S> findOne(Example<S> example) {
		return baseDao.findOne(example);
	}

	@Override
	public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
		return baseDao.findAll(example, pageable);
	}

	@Override
	public <S extends T> long count(Example<S> example) {
		return baseDao.count(example);
	}

	@Override
	public <S extends T> boolean exists(Example<S> example) {
		return baseDao.exists(example);
	}
	
	@Override
	public Pager<T> findNavigator(Pager<T> pager) {
		return baseDao.findNavigator(pager);
	}
	
	/**
	 * 设置创建信息
	 * @param entity
	 */
	private void setCreateInfo(final T entity){
		Class<?> baseModel = entity.getClass().getSuperclass();
		if(baseModel!=null && baseModel.getName().equals(BaseModel.class.getName())){
			Object user = currentUser.getCurrentUser();
			Field createDate = ReflectionUtils.findField(baseModel, "createDate");
			if(createDate!=null){
				createDate.setAccessible(true);
				ReflectionUtils.setField(createDate, entity, new Date());
				createDate.setAccessible(false);
			}
			Field createUser = ReflectionUtils.findField(baseModel, "createUser");
			if(createUser != null && user != null){
				Field id = ReflectionUtils.findField(user.getClass(), "id");
				if(id != null){
					ReflectionUtils.setField(createUser, entity, ReflectionUtils.getField(id, user));
					id.setAccessible(true);
					createUser.setAccessible(true);
					ReflectionUtils.setField(createUser, entity, ReflectionUtils.getField(id, currentUser));
					createUser.setAccessible(false);
					id.setAccessible(false);
				}
			}
		}
	}
	
	/**
	 * 设置更新信息
	 * @param entity
	 */
	private void setUpdateInfo(final T entity){
		Class<?> baseModel = entity.getClass().getSuperclass();
		if(baseModel!=null && baseModel.getName().equals(BaseModel.class.getName())){
			Object user = currentUser.getCurrentUser();
			Field updateDate = ReflectionUtils.findField(baseModel, "updateDate");
			if(updateDate!=null){
				updateDate.setAccessible(true);
				ReflectionUtils.setField(updateDate, entity, new Date());
				updateDate.setAccessible(false);
			}
			Field updateUser = ReflectionUtils.findField(baseModel, "updateUser");
			if(updateUser != null && user != null){
				Field id = ReflectionUtils.findField(user.getClass(), "id");
				if(id != null){
					ReflectionUtils.setField(updateUser, entity, ReflectionUtils.getField(id, user));
					id.setAccessible(true);
					updateUser.setAccessible(true);
					ReflectionUtils.setField(updateUser, entity, ReflectionUtils.getField(id, currentUser));
					updateUser.setAccessible(false);
					id.setAccessible(false);
				}
			}
		}
	}

}
