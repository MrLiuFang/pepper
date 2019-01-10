package com.pepper.core.base;

import java.io.Serializable;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.pepper.core.base.curd.DeleteRepository;
import com.pepper.core.base.curd.SaveRepository;
import com.pepper.core.base.curd.SelectRepository;
import com.pepper.core.base.curd.UpdateRepository;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 * @param <T>
 */
public interface BaseDao<T> extends UpdateRepository<T> ,SelectRepository<T>,SaveRepository<T>,DeleteRepository<T> , JpaRepositoryImplementation<T, Serializable> {

	

}
