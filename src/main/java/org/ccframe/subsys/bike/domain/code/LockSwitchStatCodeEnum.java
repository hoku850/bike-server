package org.ccframe.subsys.bike.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

public enum LockSwitchStatCodeEnum implements ICodeEnum {
	
	/**
	 * 不使用
	 */
	NO_USE,
	
	/**
	 * 关闭
	 */
	CLOCK,
	
	/**
	 * 打开
	 */
	OPEN;

	public static LockSwitchStatCodeEnum fromCode(String code) {
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
		for(LockSwitchStatCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
	
}
