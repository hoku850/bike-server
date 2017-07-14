package org.ccframe.subsys.bike.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

/**
 * 单车类型代码
 *
 * @author wj
 */
public enum BykeTypeCodeEnum implements ICodeEnum{

    /**
     * 蓝牙和GRPS
     */
	BLE_GPRS,
	/**
	 * 蓝牙	
	 */
	BLE,
	/**
	 * GRPS
	 */
	GPRS;

	public static BykeTypeCodeEnum fromCode(String code) {
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
		for(BykeTypeCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
}
