package org.ccframe.subsys.bike.domain.code;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICodeEnum;

public enum PayTypeZhCodeEnum implements ICodeEnum {

	/*
	 * 支付宝充值
	 */
	ALIPAY_CHARGE("支付宝充值"),

	/*
	 * 银联充值
	 */
	UNIONPAY_CHARGE("银联充值"),
	
	/*
	 * 微信充值
	 */
	WECHAT_CHARGE("微信充值");
	

    private String code;

    private PayTypeZhCodeEnum(String code) {
        this.code = code;
    }

    public static PayTypeZhCodeEnum fromCode(String code) {
        for (PayTypeZhCodeEnum boolCodeEnum : PayTypeZhCodeEnum.values()) {
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
		for(PayTypeZhCodeEnum value: values()){
			result.add(value);
		}
		return result;
	}
 
}
