package org.ccframe.subsys.bike.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

public enum SmartLockStatCodeEnum implements ICodeEnum {
	
	/**
	 * 未生产
	 */
	UNPRODUCE,
	
	/**
	 * 已生产
	 */
	PRODUCED,
	
	/**
	 * 已发放
	 */
	GRANTED,
	
	/**
	 * 已激活
	 */
	ACTIVED,
	
	/**
	 * 维修中
	 */
	TO_FIX,
	
	/**
	 * 已废弃
	 */
	DESERTED;

	public static SmartLockStatCodeEnum fromCode(String code) {
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
		for(SmartLockStatCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
	
}
