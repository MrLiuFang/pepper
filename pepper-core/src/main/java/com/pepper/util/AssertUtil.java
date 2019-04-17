/**
 * Project Name:neptune-core
 * File Name:AssertKit.java
 * Package Name:com.neptune.core.kit
 * Date:2017年12月1日下午5:33:42
 * Copyright (c) 2017, www.qi-cloud.com All Rights Reserved.
 *
 */

package com.pepper.util;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;
import com.pepper.core.exception.BusinessException;

/**
 * 断言工具类
 *
 * @author wwcong
 *
 */
public class AssertUtil {
	
	/**
	 * 判断一个字符串是否超过最大长度</br>
	 * 注意：如果字符串为空，抛java.lang.IllegalArgumentException异常</br>
	 * 参数不允许为空</br>
	 *
	 * @param str
	 *            字符串
	 * @param maxLength
	 *            最大长度
	 * @param message
	 *            抛出的异常信息
	 */
	public static void maxLength(final String str,final int maxLength,final String message) {
		if (!StringUtils.hasText(str)) {
			new BusinessException("参数不允许为空.");
		}
		if (str.length() > maxLength) {
			new BusinessException(message);
		}
	}

	/**
	 * 判断一个字段是否合法的字段名称</br>
	 * 合法的字段名称必需以a-z或者A-Z开头
	 *
	 * @param fieldName
	 *            字段名称
	 * @param message
	 *            抛出的异常信息
	 */
	public static void assertFieldName(final String fieldName,final String message) {
		if (!StringUtils.hasText(fieldName)) {
			new BusinessException(message);
		}
		final boolean isFieldName = fieldName.matches("^[a-zA-Z]+$");
		if (!isFieldName) {
			new BusinessException(message);
		}
	}

	/**
	 * 判断一个对象是否为空</br>
	 *
	 * @param obj
	 *            对象
	 * @param message
	 *            如果为空,抛出的异常信息
	 */
	public static void notNull(final Object obj,final String message) {
		if (obj == null) {
			new BusinessException(message);
		}
	}

	/**
	 * 判断一个字符串是否为空</br>
	 *
	 * @param str
	 *            字符串
	 * @param message
	 *            如果为空,抛出的异常信息
	 */
	public static void notEmpty(final String str,final String message) {
		if (!StringUtils.hasText(str)) {
			new BusinessException(message);
		}
	}



	/**
	 * 判断set是否为空，为空则抛异常</br>
	 *
	 * @param set
	 *            列表
	 * @param message
	 *            如果set为空，抛出的异常信息
	 */
	public static void notEmpty(final Set<?> set,final String message) {
		if (set == null || set.isEmpty()) {
			new BusinessException(message);
		}
	}

	/**
	 * 判断一个整数，是否大于0</br>
	 *
	 * @param num
	 *            整数
	 * @param message
	 *            为空，或者小于0，抛出的异常信息
	 */
	public static void greatZero(final Integer num,final String message) {
		if (num == null || num <= 0) {
			new BusinessException(message);
		}
	}

	/**
	 * 判断一个Long，是否大于0</br>
	 *
	 * @param num
	 *            Long
	 * @param message
	 *            为空，或者小于0，抛出的异常信息
	 */

	public static void greatZero(final Long num,final String message) {
		if (num == null || num <= 0) {
			new BusinessException(message);
		}
	}

	/**
	 * 判断二个整数是否相等 </br>
	 *
	 * @param num1
	 *            整数1
	 * @param num2
	 *            整数2
	 * @param message
	 *            不相等时，抛出的异常信息
	 */
	public static void equals(final int num1,final int num2,final String message) {
		if (num1 != num2) {
			new BusinessException(message);
		}
	}

	/**
	 * 判断二个字符串是否相等，忽略大小写</br>
	 *
	 * @param str1
	 *            字符串1
	 * @param str2
	 *            字符串2
	 * @param message
	 *            不相等时，抛出的异常信息
	 */
	public static void equalsIgnoreCase(final String str1,final String str2,final String message) {
		if (!str1.equalsIgnoreCase(str2)) {
			new BusinessException(message);
		}
	}

	/**
	 * 判断是否手机号码
	 *
	 * @param mobile
	 * @param message
	 */
	public static void assertMobile(final String mobile,final String message) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		String s2 = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";// 验证手机号
		if (StringUtils.hasText(mobile)) {
			p = Pattern.compile(s2);
			m = p.matcher(mobile);
			b = m.matches();
		}
		if (!b) {
			new BusinessException(message);
		}
	}

	/**
	 * 判断一个布尔值是否为真,不为真抛异常 </br>
	 *
	 * @param flag
	 *            列表
	 * @param message
	 *            如果FALSE，抛出的异常信息
	 */

	public static void assertTrue(final boolean flag,final String message) {
		if (!flag) {
			new BusinessException(message);
		}
	}
}
