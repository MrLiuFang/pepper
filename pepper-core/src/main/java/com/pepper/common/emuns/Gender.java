package com.pepper.common.emuns;

import com.fasterxml.jackson.annotation.JsonValue;
import com.pepper.core.IEnum;

/**
 * 性别公共枚举
 *
 * @author Mr.Liu
 *
 */
public enum Gender implements IEnum {
	FEMALE(0, "女性"), MALE(1, "男性");

	private final int key;

	private final String name;

	private Gender(int key, String name) {
		this.key = key;
		this.name = name;
	}

	@Override
	@JsonValue
	public Integer getKey() {
		return key;
	}

	@Override
	public String getName() {
		return name;
	}

	public static Gender get(int key) {
		for (Gender e : Gender.values()) {
			if (e.getKey() == key) {
				return e;
			}
		}
		return FEMALE;
	}

}
