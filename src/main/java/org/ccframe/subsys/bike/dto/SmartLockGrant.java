package org.ccframe.subsys.bike.dto;

public class SmartLockGrant {
	
	private Long startLockerHardwareCode;
	private Long endLockerHardwareCode;
	private String startBikePlateNumber;
	private String endBikePlateNumber;
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
	
	public Long getStartLockerHardwareCode() {
		return startLockerHardwareCode;
	}
	public void setStartLockerHardwareCode(Long startLockerHardwareCode) {
		this.startLockerHardwareCode = startLockerHardwareCode;
	}
	public Long getEndLockerHardwareCode() {
		return endLockerHardwareCode;
	}
	public void setEndLockerHardwareCode(Long endLockerHardwareCode) {
		this.endLockerHardwareCode = endLockerHardwareCode;
	}
	public String getStartBikePlateNumber() {
		return startBikePlateNumber;
	}
	public void setStartBikePlateNumber(String startBikePlateNumber) {
		this.startBikePlateNumber = startBikePlateNumber;
	}
	public String getEndBikePlateNumber() {
		return endBikePlateNumber;
	}
	public void setEndBikePlateNumber(String endBikePlateNumber) {
		this.endBikePlateNumber = endBikePlateNumber;
	}
	
}
