package org.ccframe.subsys.bike.domain.entity;

/**
 * 后台管理员用户.
 * @author zjm
 */
public class MemberUser {

	private Integer userId;

	private Integer orgId;
	
	public MemberUser(){}
	
	public MemberUser(Integer userId, Integer orgId) {
		super();
		this.userId = userId;
		this.orgId = orgId;
	}



	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	
}