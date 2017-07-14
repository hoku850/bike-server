package org.ccframe.subsys.core.dto;

import org.ccframe.subsys.core.domain.entity.MemberAccount;

public class MemberAccountRowDto extends MemberAccount {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4008428849530824828L;

	private String userNm;
	
	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	// 账户管理下 充值窗口使用
	private String orgNm;
	private String chargeOperateTypeCode;
	private Double changeValue;
	private String reason;
	
	public String getOrgNm() {
		return orgNm;
	}

	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}

	public String getChargeOperateTypeCode() {
		return chargeOperateTypeCode;
	}

	public void setChargeOperateTypeCode(String chargeOperateTypeCode) {
		this.chargeOperateTypeCode = chargeOperateTypeCode;
	}

	public Double getChangeValue() {
		return changeValue;
	}

	public void setChangeValue(Double changeValue) {
		this.changeValue = changeValue;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
