package org.ccframe.subsys.core.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

public enum AccountTypeCodeEnum implements ICodeEnum {

    /**
	 * 积分
	 */
	INTEGRAL,

    /**
	 * 预存款
	 */
	PRE_DEPOSIT,

    /**
	 * 押金。 单车项目覆盖
	 */
	DEPOSIT;

	public static AccountTypeCodeEnum fromCode(String code) {
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
		for(AccountTypeCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}

}
