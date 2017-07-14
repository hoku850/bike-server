package org.ccframe.subsys.core.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;


public enum ParamTypeCodeEnum implements ICodeEnum {
    /**
	 * 文本输入（值会trim过）	
	 */
	TEXT,

    /**
	 * HTML输入
	 */
	HTML,

    /**
	 * 开关类型(Y/N)
	 */
	SWITCH,
	
    /**
	 * 单选
	 */
	SINGLE_SELECT,
	
    /**
	 * 多选
	 */
	MULTI_SELECT;

	public static ParamTypeCodeEnum fromCode(String code) {
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
		for(ParamTypeCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
	
}
