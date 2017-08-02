package org.ccframe.subsys.bike.dto;

import org.ccframe.subsys.bike.domain.entity.UserToRepairRecord;

public class UserToRepairRecordRowDto extends UserToRepairRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orgNm;

	private String hardwareCodeStr;

	public String getOrgNm() {
		return orgNm;
	}

	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}

	public String getHardwareCodeStr() {
		return hardwareCodeStr;
	}

	public void setHardwareCodeStr(String hardwareCodeStr) {
		this.hardwareCodeStr = hardwareCodeStr;
	}

}
