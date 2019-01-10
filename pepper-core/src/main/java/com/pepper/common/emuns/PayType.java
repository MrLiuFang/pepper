package com.pepper.common.emuns;

import com.fasterxml.jackson.annotation.JsonValue;
import com.pepper.core.IEnum;

/**
 * 支付方式
 *
 * @author mrliu
 *
 */
public enum PayType implements IEnum {

	WX(0, "微信"), ALIPAY(1, "支付宝"), ALLINPAY(2, "通联"), UNIONPAY(3, "银联");

	private final int key;

	private final String name;

	private PayType(int key, String name) {
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

	public static PayType get(int key) {
		for (PayType e : PayType.values()) {
			if (e.getKey() == key) {
				return e;
			}
		}
		return WX;
	}
}
