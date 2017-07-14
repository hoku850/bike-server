package org.ccframe.subsys.bike.dto;

public class SmartLockGrant {
	
	private String startLockerHardwareCode;
	private String endLockerHardwareCode;
	private String bikePlateNumberPrefixText;
	private Integer orgId;
	private Integer bikeTypeId;
	
	public Integer getBikeTypeId() {
		return bikeTypeId;
	}
	public void setBikeTypeId(Integer bikeTypeId) {
		this.bikeTypeId = bikeTypeId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public String getStartLockerHardwareCode() {
		return startLockerHardwareCode;
	}
	public void setStartLockerHardwareCode(String startLockerHardwareCode) {
		this.startLockerHardwareCode = startLockerHardwareCode;
	}
	public String getEndLockerHardwareCode() {
		return endLockerHardwareCode;
	}
	public void setEndLockerHardwareCode(String endLockerHardwareCode) {
		this.endLockerHardwareCode = endLockerHardwareCode;
	}
	public String getBikePlateNumberPrefixText() {
		return bikePlateNumberPrefixText;
	}
	public void setBikePlateNumberPrefixText(String bikePlateNumberPrefixText) {
		this.bikePlateNumberPrefixText = bikePlateNumberPrefixText;
	}
	
	
}
