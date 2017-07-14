package org.ccframe.subsys.bike.dto;

import java.util.Date;

public class SmartLockListReq {
	
	private String searchText;
	private String smartLockStatCode;
	private Integer orgId;
	
	private String imeiCode;
	private String macAddress;
	private String lockerHardwareCode;
	private String bikePlateNumber;
	private Integer smartLockId;
	private Date activeDate;
	private Integer bikeTypeId;
	private Date lastUseDate;
	
	
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	
	public Integer getSmartLockId() {
		return smartLockId;
	}
	public void setSmartLockId(Integer smartLockId) {
		this.smartLockId = smartLockId;
	}
	public String getImeiCode() {
		return imeiCode;
	}
	public void setImeiCode(String imeiCode) {
		this.imeiCode = imeiCode;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public String getLockerHardwareCode() {
		return lockerHardwareCode;
	}
	public void setLockerHardwareCode(String lockerHardwareCode) {
		this.lockerHardwareCode = lockerHardwareCode;
	}
	public String getBikePlateNumber() {
		return bikePlateNumber;
	}
	public void setBikePlateNumber(String bikePlateNumber) {
		this.bikePlateNumber = bikePlateNumber;
	}
	public Date getActiveDate() {
		return activeDate;
	}
	public void setActiveDate(Date activeDate) {
		this.activeDate = activeDate;
	}
	public Integer getBikeTypeId() {
		return bikeTypeId;
	}
	public void setBikeTypeId(Integer bikeTypeId) {
		this.bikeTypeId = bikeTypeId;
	}

	public String getSmartLockStatCode() {
		return smartLockStatCode;
	}
	public void setSmartLockStatCode(String smartLockStatCode) {
		this.smartLockStatCode = smartLockStatCode;
	}
	public Date getLastUseDate() {
		return lastUseDate;
	}
	public void setLastUseDate(Date lastUseDate) {
		this.lastUseDate = lastUseDate;
	}
	
	
}
