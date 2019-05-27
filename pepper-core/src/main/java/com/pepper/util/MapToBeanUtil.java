package com.pepper.util;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtilsBean;
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

	public static synchronized void convert(Object obj, final Map<String, Object> map) {
		try {
			ConvertUtils.deregister(Date.class);
			DateTimeConverter dtConverter = new DateTimeConverter();
			ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
//			BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean, new PropertyUtilsBean());
			convertUtilsBean.register(dtConverter, Date.class);
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

class DateTimeConverter implements Converter {
	private static final String DATE = "yyyy-MM-dd";
	private static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
	private static final String TIMESTAMP = "yyyy-MM-dd HH:mm:ss.SSS";

	@Override
	public Object convert(Class type, Object value) {
		// TODO Auto-generated method stub
		return toDate(type, value);
	}

	public static Object toDate(Class type, Object value) {
		if (value == null || "".equals(value))
			return null;
		if (value instanceof String) {
			String dateValue = value.toString().trim();
			int length = dateValue.length();
			if (type.equals(java.util.Date.class)) {
				try {
					DateFormat formatter = null;
					if (length <= 10) {
						formatter = new SimpleDateFormat(DATE, new DateFormatSymbols(Locale.CHINA));
						return formatter.parse(dateValue);
					}
					if (length <= 19) {
						formatter = new SimpleDateFormat(DATETIME, new DateFormatSymbols(Locale.CHINA));
						return formatter.parse(dateValue);
					}
					if (length <= 23) {
						formatter = new SimpleDateFormat(TIMESTAMP, new DateFormatSymbols(Locale.CHINA));
						return formatter.parse(dateValue);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return value;
	}
}