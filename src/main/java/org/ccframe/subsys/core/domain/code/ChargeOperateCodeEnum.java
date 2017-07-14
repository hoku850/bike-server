package org.ccframe.subsys.core.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

public enum ChargeOperateCodeEnum implements ICodeEnum {

    /**
	 * 充值
	 */
	RECHARGE,

    /**
	 * 扣费
	 */
	CHARGING;

	public static ChargeOperateCodeEnum fromCode(String code) {
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
		for(ChargeOperateCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}

}
