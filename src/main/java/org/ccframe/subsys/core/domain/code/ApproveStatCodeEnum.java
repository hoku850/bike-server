package org.ccframe.subsys.core.domain.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;


public enum ApproveStatCodeEnum implements ICodeEnum {
    /**
	 * 未提交
	 */
	NOT_SUBMIT,

    /**
	 * 未处理
	 */
	QUEUE,

    /**
	 * 同意
	 */
	APPROVE,
	
    /**
	 * 拒绝
	 */
	DENY;

	public static ApproveStatCodeEnum fromCode(String code) {
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
		for(ApproveStatCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
	
}
