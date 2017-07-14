package org.ccframe.subsys.core.dto;

/**
 * 文章列表请求条件
 * 
 * @author Jim
 *
 */
public class MemberAccountListReq {

	private String searchText;
	private String accountTypeCode;
	private Integer orgId;

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getAccountTypeCode() {
		return accountTypeCode;
	}

	public void setAccountTypeCode(String accountTypeCode) {
		this.accountTypeCode = accountTypeCode;
	}

}
