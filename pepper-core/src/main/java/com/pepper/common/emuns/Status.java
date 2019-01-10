package com.pepper.common.emuns;

import com.fasterxml.jackson.annotation.JsonValue;
import com.pepper.core.IEnum;

/**
 * 状态枚举
 *
 * @author Mr.Liu
 *
 */
public enum Status implements IEnum {
	NORMAL(0, "禁用"), DISABLE(1, "正常");

	private final int key;

	private final String name;

	private Status(int key, String name) {
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

	public static Status get(int key) {
		for (Status e : Status.values()) {
			if (e.getKey() == key) {
				return e;
			}
		}
		return NORMAL;
	}
}
