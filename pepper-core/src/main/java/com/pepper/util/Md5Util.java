package com.pepper.util;

import java.security.MessageDigest;

import org.springframework.util.StringUtils;

public class Md5Util {
	// 公盐
	public static final String PUBLIC_SALT = "pepper";
	// 十六进制下数字到字符的映射数组
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
			"e", "f"};

	/**
	 * 用户密码加密，盐值为 ：私盐+公盐
	 * 
	 * @param password
	 *            密码
	 * @param salt
	 *            私盐
	 * @return MD5加密字符串
	 */
	public static String encryptPassword(final String password,final String salt) {
		return encodeByMD5(PUBLIC_SALT + password + salt);
	}

	/**
	 * 密码加密 为了避免登录时前端传过来的密码是明文，所以前端密码传过来就经过了一次md5加密，所以登录密码只需要再执行一次
	 * encodeByMD5(PUBLIC_SALT + pwd);即是数据库密码，而普通的明文密码，需要两次md5加密。
	 * 
	 * @param password
	 * @return
	 */
	public static String encryptPassword(final String password) {
		return encodeByMD5(PUBLIC_SALT + password);
	}

	/**
	 * md5加密算法（结果转大写）
	 * 
	 * @param originString
	 * @return
	 */
	public synchronized static String encodeByMD5(final String originString) {
		String res = encodeByMD5Real(originString);
		if (StringUtils.hasText(res)) {
			return res.toUpperCase();
		}
		return res;
	}

	/**
	 * md5加密算法（单纯md5）
	 * 
	 * @param originString
	 * @return
	 */
	public synchronized static String encodeByMD5Real(final String originString) {
		if (originString != null) {
			try {
				// 创建具有指定算法名称的信息摘要
				MessageDigest md = MessageDigest.getInstance("MD5");
				// 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
				byte[] results = md.digest(originString.getBytes());
				// 将得到的字节数组变成字符串返回
				String resultString = byteArrayToHexString(results);
				return resultString;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 转换字节数组为十六进制字符串
	 * 
	 * @param 字节数组
	 * @return 十六进制字符串
	 */
	private static String byteArrayToHexString(final byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/** 将一个字节转化成十六进制形式的字符串 */
	private static String byteToHexString(final byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static void main(String agr[]) {
		System.out.println(Md5Util.encodeByMD5("123456"));
		System.out.println(Md5Util.encryptPassword(Md5Util.encodeByMD5("123456"),"test1"));
	}
}