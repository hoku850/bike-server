package org.ccframe.subsys.bike.dto;


/**
 * 骑行订单请求参数
 * 
 * @author 梁期智
 *
 */
public class ChargeOrderListReq {
	
	private Integer orgId; // 运营商ID
	private String chargeOrderStatCode; // 订单状态
	private String paymentTypeCode; // 支付类型
	
	private String searchText;

	@Override
	public String toString() {
		return "ChargeOrderListReq [orgId=" + orgId + ", chargeOrderStatCode="
				+ chargeOrderStatCode + ", paymentTypeCode=" + paymentTypeCode
				+ ", searchText=" + searchText + "]";
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getChargeOrderStatCode() {
		return chargeOrderStatCode;
	}

	public void setChargeOrderStatCode(String chargeOrderStatCode) {
		this.chargeOrderStatCode = chargeOrderStatCode;
	}

	public String getPaymentTypeCode() {
		return paymentTypeCode;
	}

	public void setPaymentTypeCode(String paymentTypeCode) {
		this.paymentTypeCode = paymentTypeCode;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
}
