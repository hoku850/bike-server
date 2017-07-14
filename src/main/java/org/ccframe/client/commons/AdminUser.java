package org.ccframe.client.commons;

import org.ccframe.subsys.core.domain.entity.User;

/**
 * 后台管理员用户.
 * 如果以后扩展对operation的限制，也在此一并返回所有的按钮操作权限。
 * @author JIM
 *
 */
public class AdminUser {

	private Integer userId;
	
	private String loginId;

	private Integer orgId;
	
	private String userNm;
	
	public AdminUser(){}
	
	public static AdminUser create(User user, Integer orgId){
		AdminUser adminUser =  new AdminUser();
		adminUser.setLoginId(user.getLoginId());
		adminUser.setUserId(user.getUserId());
		adminUser.setUserNm(user.getUserNm());
		adminUser.setOrgId(orgId);
		return adminUser;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	
}
