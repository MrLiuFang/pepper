package com.pepper.util;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.StringConverter;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;

/**
 * 
 * @author liufang
 *
 */
public class MapToBeanUtil {

	public MapToBeanUtil() {
		super();
	}

	public static synchronized void convert(Object obj,final Map<String, Object> map) {
		try {
			ConvertUtils.register(new DateLocaleConverter(Locale.CHINA), Date.class);
			ConvertUtils.register(new StringConverter(null), String.class);
			ConvertUtils.register(new LongConverter(null), Long.class);
			ConvertUtils.register(new BooleanConverter(null), Boolean.class);
			ConvertUtils.register(new DoubleConverter(null), Double.class);
			ConvertUtils.register(new IntegerConverter(null), Integer.class);
			ConvertUtils.register(new FloatConverter(null), Float.class);
			ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
			BeanUtils.populate(obj, map);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
