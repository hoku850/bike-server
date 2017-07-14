package org.ccframe.subsys.bike.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

public enum PaymentTypeCodeEnum implements ICodeEnum {
	
	/**
	 * 支付宝
	 */
	ALIPAY,
	
	/**
	 * 微信 
	 */
	WECHAT,
	
	/**
	 * 中国银联 
	 */
	UNIONPAY;
	
	public static PaymentTypeCodeEnum fromCode(String code) {
		try {
			return values()[Integer.parseInt(code)];
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String toCode() {
		return Integer.toString(this.ordinal());
	}

	@Override
	public List<ICodeEnum> valueList() {
		List<ICodeEnum> result = new ArrayList<ICodeEnum>();
		for(PaymentTypeCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
	
}
