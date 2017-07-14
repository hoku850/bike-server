package org.ccframe.subsys.bike.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

public enum ChargeOrderStatCodeEnum implements ICodeEnum {
		
	/**
	 * 待支付
	 */
	UNPAID,
	
	/**
	 * 充值成功
	 */
	CHARGE_SUCCESS,
	
	/**
	 * 充值失败
	 */
	CHARGE_FAILED,
	
	
	/**
	 * 退款中
	 */
	REFUNDING,
	
	/**
	 * 退款成功
	 */
	REFUND_SUCCESS,
	
	/**
	 * 退款失败
	 */
	REFUND_FAILED;

	public static ChargeOrderStatCodeEnum fromCode(String code) {
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
		for(ChargeOrderStatCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
	
}
