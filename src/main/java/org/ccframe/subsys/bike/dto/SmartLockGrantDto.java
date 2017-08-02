package org.ccframe.subsys.bike.dto;

public class SmartLockGrantDto {
	private long totalLock;
	private String orgNm;
	private String smartLockIdStr = "50001";
	
	
	public String getSmartLockIdStr() {
		return smartLockIdStr;
	}
	public void setSmartLockIdStr(String smartLockIdStr) {
		this.smartLockIdStr = smartLockIdStr;
	}
	public long getTotalLock() {
		return totalLock;
	}
	public void setTotalLock(long totalLock) {
		this.totalLock = totalLock;
	}
	public String getOrgNm() {
		return orgNm;
	}
	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	
	
}
