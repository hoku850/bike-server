package org.ccframe.subsys.bike.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

public enum ValidateCodeStatCodeEnum implements ICodeEnum {
	/**
	 * 验证通过
	 */
	PASS,
	/**
	 * 验证码过期
	 */
	TIMEOUT,
	/**
	 * 验证码错误
	 */
	ERROR;
	@Override
	public String toCode() {
		return Integer.toString(this.ordinal());
	}

	@Override
	public List<ICodeEnum> valueList() {
		List<ICodeEnum> result = new ArrayList<ICodeEnum>();
		for(ValidateCodeStatCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
	

}
