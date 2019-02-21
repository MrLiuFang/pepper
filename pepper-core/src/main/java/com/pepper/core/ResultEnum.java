package com.pepper.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * @author mrliu
 *
 */
public class ResultEnum {

	public enum Code {
		URI_INVALID(-100, "uri无效"), LOGIC_ERROR(-200, "逻辑错误"), SYSTEM_ERROR(-300, "系统错误"), IS_NOT_LOGIN(-400,
				"用户没有登录"), SUCCESS(200, "成功"), FAIL(-900, "失败"), NO_PERMISSION(-600, "无权限");

		private final int key;

		private final String name;

		private Code(int key, String name) {

			this.key = key;
			this.name = name;
		}

		@JsonValue
		public int getKey() {

			return key;
		}

		public String getName() {

			return name;
		}

		public static String getDesc(int state) {

			Code[] codes = Code.values();
			for (Code code : codes) {
				if (code.getKey() == state) {
					return code.getName();
				}
			}
			return "";
		}

		public static String getName(int state) {

			Code[] codes = Code.values();
			for (Code code : codes) {
				if (code.getKey() == state) {
					return code.toString();
				}
			}
			return "";
		}

		public static List<Map<String, String>> getKeyValue() {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			Code[] codes = Code.values();
			for (Code code : codes) {
				Map<String, String> map = new HashMap<String, String>();
				map.put(code.toString(), code.getName());
				list.add(map);
			}
			return list;
		}
	}

	public static void main(String[] args) {
		System.out.println(Code.getDesc(-100));
		System.out.println(Code.getName(-100));
		System.out.println(Code.URI_INVALID.getKey());
		System.out.println(Code.URI_INVALID.getName());
		System.out.println(Code.URI_INVALID.key);
		System.out.println(Code.URI_INVALID.name);
		System.out.println(Code.URI_INVALID.getKey());
		System.out.println(Code.getKeyValue());
	}
}
