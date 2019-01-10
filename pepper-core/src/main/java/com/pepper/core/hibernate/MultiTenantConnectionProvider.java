package com.pepper.core.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 这个类是Hibernate框架拦截sql语句并在执行sql语句之前更换数据源提供的类
 * 
 * @author
 * @version 1.0
 */
public class MultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5419497103710611516L;

	private static Map<String, DataSource> dataSourceMap = new HashMap<>();

	public static final String DEFAULT_DATASOURCE = "DEFAULT";

	// 在没有提供tenantId的情况下返回默认数据源
	@Override
	protected DataSource selectAnyDataSource() {
		if (!dataSourceMap.containsKey(DEFAULT_DATASOURCE)) {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("url", System.getProperty("spring.datasource.url"));
			map.put("user_name", System.getProperty("spring.datasource.username"));
			map.put("password", System.getProperty("spring.datasource.password"));
			createDataSource(DEFAULT_DATASOURCE, map);
		}
		return dataSourceMap.get(DEFAULT_DATASOURCE);
	}

	// 提供了tenantId的话就根据ID来返回数据源
	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		DataSource dataSource = dataSourceMap.get(tenantIdentifier);
		if (dataSource == null) {
			setDataSource(tenantIdentifier);
		}
		return dataSourceMap.get(tenantIdentifier);
	}

	/**
	 * 设置数据源
	 * 
	 * @param tenantIdentifier
	 */
	private void setDataSource(String tenantIdentifier) {
		DataSource dataSource = selectAnyDataSource();
		try {
			Connection connection = dataSource.getConnection();
			PreparedStatement statement = connection
					.prepareStatement("select * from t_tenant_info where tenant_id = ? ");
			statement.setString(1, tenantIdentifier);
			ResultSet resultSet = statement.executeQuery();
			ResultSetMetaData data = resultSet.getMetaData();
			while (resultSet.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= data.getColumnCount(); i++) {
					String c = data.getColumnName(i);
					Object v = resultSet.getObject(c);
					map.put(c, v);
				}
				createDataSource(tenantIdentifier, map);
			}

			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param tenantIdentifier
	 * @param list
	 */
	private void createDataSource(String tenantIdentifier, Map<String, Object> map) {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(map.get("url").toString());
		dataSource.setUsername(map.get("user_name").toString());
		dataSource.setPassword(map.get("password").toString());
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//		dataSource.setInitialSize(5);
//		dataSource.setMinIdle(1);
		dataSource.setMaxActive(10);
		dataSourceMap.put(tenantIdentifier, dataSource);
	}
}
