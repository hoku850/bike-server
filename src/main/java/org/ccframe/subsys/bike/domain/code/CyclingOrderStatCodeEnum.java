package org.ccframe.subsys.bike.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

public enum CyclingOrderStatCodeEnum implements ICodeEnum{
		
	/**
	 * 骑行中
	 */
	ON_THE_WAY,
	
	/**
	 * 骑行完成
	 */
	CYCLING_FINISH,
	
	/**
	 * 支付完成（完成）
	 */
	PAY_FINISH,
		
	/**
	 * 已报修
	 */
	TO_BE_REPAIRED,

	/**
	 * 临时锁定
	 */
	TEMPORARY_LOCKING;

	public static CyclingOrderStatCodeEnum fromCode(String code) {
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
		for(CyclingOrderStatCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
}
