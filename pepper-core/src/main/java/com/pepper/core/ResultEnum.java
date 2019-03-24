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

	public enum Status {
		URI_INVALID(-100, "uri无效"),LOGIN_FAIL(401, "登录失败"), LOGIC_ERROR(-200, "逻辑错误"), SYSTEM_ERROR(-300, "系统错误"), IS_NOT_LOGIN(-400,
				"用户没有登录"), SUCCESS(200, "成功"), FAIL(-900, "失败"), NO_PERMISSION(-600, "无权限");

		private final int key;

		private final String name;

		private Status(int key, String name) {

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

			Status[] statuss = Status.values();
			for (Status status : statuss) {
				if (status.getKey() == state) {
					return status.getName();
				}
			}
			return "";
		}

		public static String getName(int state) {

			Status[] statuss = Status.values();
			for (Status status : statuss) {
				if (status.getKey() == state) {
					return status.toString();
				}
			}
			return "";
		}

		public static List<Map<String, String>> getKeyValue() {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			Status[] statuss = Status.values();
			for (Status status : statuss) {
				Map<String, String> map = new HashMap<String, String>();
				map.put(status.toString(), status.getName());
				list.add(map);
			}
			return list;
		}
	}

}
