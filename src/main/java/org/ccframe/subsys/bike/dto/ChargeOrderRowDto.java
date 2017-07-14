package org.ccframe.subsys.bike.dto;

import org.ccframe.subsys.bike.domain.code.ChargeOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.code.PaymentTypeCodeEnum;
import org.ccframe.subsys.bike.domain.entity.ChargeOrder;

public class ChargeOrderRowDto extends ChargeOrder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6717254207360426962L;

	private String orgNm;

	public String getOrgNm() {
		return orgNm;
	}

	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}

	public String getPaymentTypeCodeStr() {
		
		switch (PaymentTypeCodeEnum.fromCode(getPaymentTypeCode())) {
		case ALIPAY:
			return "支付宝";
		case WECHAT:
			return "微信";
		case UNIONPAY:
			return "中国银联 ";
		default:
			return "NULL";
		}
	}

	public String getChargeOrderStatCodeStr() {
		
		switch (ChargeOrderStatCodeEnum.fromCode(getChargeOrderStatCode())) {
		case UNPAID:
			return "待支付";
		case CHARGE_SUCCESS:
			return "充值成功";
		case CHARGE_FAILED:
			return "充值失败";
		case REFUNDING:
			return "退款中";
		case REFUND_SUCCESS:
			return "退款成功";
		case REFUND_FAILED:
			return "退款失败";
		default:
			return "NULL";
		}
	}
}
