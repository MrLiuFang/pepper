package com.pepper.common.emuns;

import com.fasterxml.jackson.annotation.JsonValue;
import com.pepper.core.IEnum;

public enum Scope implements IEnum {
	PC(0, "前端"), CONSOLE(1, "后台"), APP(2, "移动端"), WEIXIN(3, "微信端");

	private final int key;

	private final String name;

	private Scope(int key, String name) {
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

	public static Scope get(int key) {
		for (Scope e : Scope.values()) {
			if (e.getKey() == key) {
				return e;
			}
		}
		return CONSOLE;
	}
	
}
