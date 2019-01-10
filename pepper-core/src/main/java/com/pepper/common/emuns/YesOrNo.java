package com.pepper.common.emuns;

import com.fasterxml.jackson.annotation.JsonValue;
import com.pepper.core.IEnum;

/**
 *
 * @author Mr.Liu
 *
 */
public enum YesOrNo implements IEnum{
	NO(0, "否"), YES(1, "是");

	private final int key;

	private final String name;

	private YesOrNo(int key, String name) {
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

	public static YesOrNo get(int key) {
		for (YesOrNo e : YesOrNo.values()) {
			if (e.getKey() == key) {
				return e;
			}
		}
		return NO;
	}
}
