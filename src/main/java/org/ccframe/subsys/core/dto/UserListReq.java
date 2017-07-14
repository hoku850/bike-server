package org.ccframe.subsys.core.dto;

import java.util.Date;

import org.ccframe.client.commons.UtilDateTimeClient;

/**
 * 文章列表请求条件
 * @author Jim
 *
 */
public class UserListReq{
	private String loginId;
	private String userNm;
	private Date createDateStart;
	private Date createDateEnd;
	private String userStatCode;
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public Date getCreateDateStart() {
		return createDateStart;
	}
	public void setCreateDateStart(Date createDateStart) {
		this.createDateStart = createDateStart;
	}
	public Date getCreateDateEnd() {
		return createDateEnd;
	}
	public void setCreateDateEnd(Date createDateEnd) {
		this.createDateEnd = createDateEnd;
	}
	public String getUserStatCode() {
		return userStatCode;
	}
	public void setUserStatCode(String userStatCode) {
		this.userStatCode = userStatCode;
	}
	//---------------------------
	public void setCreateDateStartStr(String createDateStartStr) {
		if(createDateStartStr != null){
			this.setCreateDateStart(UtilDateTimeClient.convertStringToDateTime(createDateStartStr));
		}
		
	}
	public void setCreateDateEndStr(String createDateEndStr) {
		if(createDateEndStr != null){
			this.setCreateDateStart(UtilDateTimeClient.convertStringToDateTime(createDateEndStr));
		}
		
	}
}
