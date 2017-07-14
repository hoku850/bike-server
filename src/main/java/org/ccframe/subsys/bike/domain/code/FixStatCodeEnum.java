package org.ccframe.subsys.bike.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

public enum FixStatCodeEnum implements ICodeEnum {
	
	/**
	 * 未处理
	 */
	UNFIX,
	
	/**
	 * 已处理
	 */
	FIXED;

	public static FixStatCodeEnum fromCode(String code) {
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
		for(FixStatCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
	
}
