package org.ccframe.subsys.bike.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

public enum ReturnPageCodeEnum implements ICodeEnum {

	/*
	 * 跳转到用车中页面
	 */
	ON_THE_WAY("onTheWay"),

	/*
	 * 跳转到订单支付页面
	 */
	WAIT_PAY("waitPay"),
	
	/*
	 * 跳转到登陆后首屏
	 */
	FIRST("first"),
	
	/*
	 * 不处理
	 */
	EMPTY_STRING("");

    private String code;

    private ReturnPageCodeEnum(String code) {
        this.code = code;
    }

    public static ReturnPageCodeEnum fromCode(String code) {
        for (ReturnPageCodeEnum boolCodeEnum : ReturnPageCodeEnum.values()) {
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
		for(ReturnPageCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
 
}
