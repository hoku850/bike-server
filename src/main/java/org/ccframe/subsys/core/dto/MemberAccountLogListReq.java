package org.ccframe.subsys.core.dto;

/**
 * 文章列表请求条件
 * 
 * @author Jim
 *
 */
public class MemberAccountLogListReq {

	private Integer userId;
	private Integer memberAccountId;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getMemberAccountId() {
		return memberAccountId;
	}

	public void setMemberAccountId(Integer memberAccountId) {
		this.memberAccountId = memberAccountId;
	}

}
