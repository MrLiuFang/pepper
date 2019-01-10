package com.pepper.core;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import com.pepper.core.base.impl.BaseDaoImpl;

/**
 * 
 * @author mrliu
 *
 * @param <R>
 * @param <T>
 * @param <I>
 */
public class BaseDaoFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
		extends JpaRepositoryFactoryBean<R, T, I> {

	public BaseDaoFactoryBean(Class<? extends R> repositoryInterface) {
		super(repositoryInterface);
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(final EntityManager entityManager) {
		return new JpaRepositoryFactory(entityManager) {

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			protected SimpleJpaRepository<T, Serializable> getTargetRepository(RepositoryInformation information,
					EntityManager entityManager) {
				Class<T> domainClass = (Class<T>) information.getDomainType();
				JpaEntityInformation<T, Serializable> entityInformation = getEntityInformation(domainClass);
				return new BaseDaoImpl(entityInformation, entityManager);
			}

			@Override
			protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
				return BaseDaoImpl.class;
			}
		};
	}
}