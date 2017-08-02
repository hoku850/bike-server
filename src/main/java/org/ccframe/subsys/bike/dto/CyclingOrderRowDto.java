package org.ccframe.subsys.bike.dto;

import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;

public class CyclingOrderRowDto extends CyclingOrder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1375546387825976209L;

	private String orgNm;

	private String bikeTypeNm;

	private String userNm;

	private String continueTimeStr;

	private String cyclingDistanceMeterStr;

	private String hardwareCodeStr;

	private String loginId;

	public String getOrgNm() {
		return orgNm;
	}

	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}

	public String getBikeTypeNm() {
		return bikeTypeNm;
	}

	public void setBikeTypeNm(String bikeTypeNm) {
		this.bikeTypeNm = bikeTypeNm;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public String getContinueTimeStr() {
		return continueTimeStr;
	}

	public void setContinueTimeStr(String continueTimeStr) {
		this.continueTimeStr = continueTimeStr;
	}

	public String getCyclingDistanceMeterStr() {
		return cyclingDistanceMeterStr;
	}

	public void setCyclingDistanceMeterStr(String cyclingDistanceMeterStr) {
		this.cyclingDistanceMeterStr = cyclingDistanceMeterStr;
	}

	public String getHardwareCodeStr() {
		return hardwareCodeStr;
	}

	public void setHardwareCodeStr(String hardwareCodeStr) {
		this.hardwareCodeStr = hardwareCodeStr;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	// 增强型方法
	public String getCyclingOrderStatCodeStr() {

		switch (CyclingOrderStatCodeEnum.fromCode(getCyclingOrderStatCode())) {
		case ON_THE_WAY:
			return "骑行中";
		case CYCLING_FINISH:
			return "骑行完成";
		case PAY_FINISH:
			return "支付完成";
		case TO_BE_REPAIRED:
			return "已报修";
		case TEMPORARY_LOCKING:
			return "锁定中";
		default:
			return "NULL";
		}

	}
}
