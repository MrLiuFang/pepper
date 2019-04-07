package com.pepper.core.base.curd;

import com.pepper.core.base.BaseDao;
import com.pepper.util.SpringContextUtil;

/**
 * 
 * @author Mr.Liu
 *
 */
public abstract class DaoExImpl<T>  {

	private volatile BaseDao<T> baseDao; 

	@SuppressWarnings("unchecked")
	protected BaseDao<T> getPepperSimpleJpaRepository(Class<?> classz) {
		if (baseDao == null) {
			synchronized (DaoExImpl.class) {
				if (baseDao == null) {
					baseDao = (BaseDao<T>) SpringContextUtil.getBean(toLowerCaseFirstOne(classz.getSimpleName().replace("Impl", "")));
				}
			}
		}
		return baseDao;
	}
	
	private String toLowerCaseFirstOne(final String str){
        if(Character.isLowerCase(str.charAt(0)))
            return str;
        else
            return (new StringBuilder()).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
    }

}
