package org.ccframe.subsys.bike.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

public enum CyclingOrderStateZhCodeEnum implements ICodeEnum {

	/*
	 * 显示已报修
	 */
	ORDER_FIXED("已报修"),

	/*
	 * 显示已完成
	 */
	ORDER_FINISHED("已完成");

    private String code;

    private CyclingOrderStateZhCodeEnum(String code) {
        this.code = code;
    }

    public static CyclingOrderStateZhCodeEnum fromCode(String code) {
        for (CyclingOrderStateZhCodeEnum boolCodeEnum : CyclingOrderStateZhCodeEnum.values()) {
            if (boolCodeEnum.code.equals(code)) {
                return boolCodeEnum;
            }
        }
        return null;
    }

    public String toCode() {
        return code;
    }

	@Override
	public List<ICodeEnum> valueList() {
		List<ICodeEnum> result = new ArrayList<ICodeEnum>();
		for(CyclingOrderStateZhCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
 
}
