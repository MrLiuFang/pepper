package com.pepper.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * rest接口入参规则
 * 
 * @author mrliu
 *
 */
public class RestRuleEnum {

	public enum Rule {
		EQUAL(0, "等于"), GREATER(1, "大于"), LESS(2, "小于"), GREATER_EQUAL(3, "大于&等于"), LESS_EQUAL(4,
				"小于&等于"), GREATER_EQUAL_LESS_EQUAL(5, "大于&等于<->小于&等于"), VAGUE(6, "模糊");

		private final int key;

		private final String name;

		private Rule(int key, String name) {

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

			Rule[] rules = Rule.values();
			for (Rule rule : rules) {
				if (rule.getKey() == state) {
					return rule.getName();
				}
			}
			return "";
		}

		public static String getName(int state) {

			Rule[] rules = Rule.values();
			for (Rule rule : rules) {
				if (rule.getKey() == state) {
					return rule.toString();
				}
			}
			return "";
		}

		public static List<Map<String, String>> getKeyVlue() {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			Rule[] rules = Rule.values();
			for (Rule rule : rules) {
				Map<String, String> map = new HashMap<String, String>();
				map.put(rule.toString(), rule.getName());
				list.add(map);
			}
			return list;
		}
	}
}
