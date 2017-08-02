package org.ccframe.subsys.bike.dto;

import org.ccframe.subsys.bike.domain.entity.SmartLock;

public class SmartLockRowDto extends SmartLock {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orgNm;
	private String bikeTypeNm;
	private String hardwareCodeStr;

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

	public String getHardwareCodeStr() {
		return hardwareCodeStr;
	}

	public void setHardwareCodeStr(String hardwareCodeStr) {
		this.hardwareCodeStr = hardwareCodeStr;
	}

}
