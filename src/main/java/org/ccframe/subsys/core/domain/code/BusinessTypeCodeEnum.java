package org.ccframe.subsys.core.domain.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;


public enum BusinessTypeCodeEnum implements ICodeEnum {
    /**
	 * 文章
	 */
	ARTICLE,

    /**
	 * 相册
	 */
	ALBUM;

	public static BusinessTypeCodeEnum fromCode(String code) {
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
		for(BusinessTypeCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}

}
