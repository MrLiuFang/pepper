package com.pepper.core.base.curd;

import java.lang.reflect.ParameterizedType;

import org.springframework.util.StringUtils;

import com.pepper.core.base.BaseDao;
import com.pepper.util.SpringContextUtil;

/**
 * 
 * @author Mr.Liu
 *
 */
public abstract class DaoExImpl<T> {

	private volatile BaseDao<T> baseDao;

	protected BaseDao<T> getPepperSimpleJpaRepository() {
		return getPepperSimpleJpaRepository(null);
	}
	
	@SuppressWarnings("unchecked")
	protected BaseDao<T> getPepperSimpleJpaRepository(String daoName) {
		if (baseDao == null) {
			synchronized (DaoExImpl.class) {
				if (baseDao == null) {
					if(!StringUtils.hasText(daoName)) {
						Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
						daoName = tClass.getName();
					}
					baseDao = (BaseDao<T>) SpringContextUtil.getBean(daoName);
				}
			}
		}
		return baseDao;
	}

}
