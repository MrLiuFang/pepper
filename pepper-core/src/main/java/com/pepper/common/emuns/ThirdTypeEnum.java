package com.pepper.common.emuns;

import com.fasterxml.jackson.annotation.JsonValue;
import com.pepper.core.IEnum;

/**
 * 第三方账号类型
 *
 */
public enum ThirdTypeEnum implements IEnum{

	WeiXin(0, "微信"), Weibo(1, "微博");

	private final int key;

	private final String name;

	private ThirdTypeEnum(int key, String name) {
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

	public static ThirdTypeEnum get(int key) {
		for (ThirdTypeEnum e : ThirdTypeEnum.values()) {
			if (e.getKey() == key) {
				return e;
			}
		}
		return WeiXin;
	}
}
