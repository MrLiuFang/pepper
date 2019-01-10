package com.pepper.common.emuns;

import com.fasterxml.jackson.annotation.JsonValue;
import com.pepper.core.IEnum;

/**
 * 支付来源
 *
 * @author mrliu
 *
 */
public enum PaySource implements IEnum {
	PC(0, "PC网站"), MOBILE_WEBSITE(1, "手机网站"), WX(2, "微信"), IOS(3, "ios"), ANDROID(4, "android");

	private final int key;

	private final String name;

	private PaySource(int key, String name) {
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

	public static PaySource get(int key) {
		for (PaySource e : PaySource.values()) {
			if (e.getKey() == key) {
				return e;
			}
		}
		return PC;
	}
}
