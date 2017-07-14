package org.ccframe.subsys.bike.dto;


public class UserToRepairRecordListReq {
	
	private String searchText;
	private Integer orgId;
	private String fixStatCode;
	

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

	public String getFixStatCode() {
		return fixStatCode;
	}

	public void setFixStatCode(String fixStatCode) {
		this.fixStatCode = fixStatCode;
	}
	
	
}
