package org.ccframe.subsys.core.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ccframe.client.commons.UtilDateTimeClient;
import org.ccframe.commons.cache.AutoCacheConfig;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

@Entity
@Table(name = "SYS_MEMBER_ACCOUNT_LOG")
@AutoCacheConfig(1) //日志表不cache
//elasticsearch
@Document(indexName = "member_account_log_index", type = "memberAccountLog")
@Setting(settingPath = "elasticsearch-analyser.json")
public class MemberAccountLog implements Serializable{
	
	public static final String MEMBER_ACCOUNT_LOG_ID = "memberAccountLogId";
	public static final String USER_ID = "userId";
	public static final String ORG_ID = "orgId";
	public static final String MEMBER_ACCOUNT_ID = "memberAccountId";
	public static final String PREV_VALUE = "prevValue";
	public static final String AFTER_VALUE = "afterValue";
	public static final String CHANGE_VALUE = "changeValue";
	public static final String SYS_TIME = "sysTime";
	public static final String REASON = "reason";
	public static final String OPERATION_MAN_ID = "operationManId";
	
	public static final String SYS_TIME_STR = "sysTimeStr";
	
	private static final long serialVersionUID = 1L;
	
	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//elasticsearch
	@org.springframework.data.annotation.Id
	@Column(name = "MEMBER_ACCOUNT_LOG_ID", nullable = false, length = 10)
	private java.lang.Integer memberAccountLogId;
	
	@Column(name = "USER_ID", nullable = false, length = 10)
	private java.lang.Integer userId;
	
	@Column(name = "ORG_ID", nullable = true, length = 10)
	private java.lang.Integer orgId;
	
	@Column(name = "MEMBER_ACCOUNT_ID", nullable = false, length = 10)
	private java.lang.Integer memberAccountId;
	
	@Column(name = "PREV_VALUE", nullable = false, length = 22)
	private java.lang.Double prevValue;
	
	@Column(name = "AFTER_VALUE", nullable = false, length = 22)
	private java.lang.Double afterValue;
	
	@Column(name = "CHANGE_VALUE", nullable = false, length = 22)
	private java.lang.Double changeValue;
	
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@Column(name = "SYS_TIME", nullable = false, length = 0)
	private java.util.Date sysTime;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") 
	@Column(name = "REASON", nullable = false, length = 1024)
	private java.lang.String reason;
	
	@Column(name = "OPERATION_MAN_ID", nullable = true, length = 10)
	private java.lang.Integer operationManId;
	
	//columns END
	

	public MemberAccountLog(){
	}

	//增强型方法satat
	public String getSysTimeStr() {
		return this.getSysTime() == null ? null : UtilDateTimeClient.convertDateTimeToMmString(this.getSysTime());
	}
	
	public void setSysTimeStr(String value) {
		if(value != null){
			this.setSysTime(UtilDateTimeClient.convertStringToDateTime(value));
		}
	}
	
	//增强型方法end
	public void setMemberAccountLogId(java.lang.Integer memberAccountLogId) {
		this.memberAccountLogId = memberAccountLogId;
	}
	
	public java.lang.Integer getMemberAccountLogId() {
		return this.memberAccountLogId;
	}
	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}
	
	public java.lang.Integer getUserId() {
		return this.userId;
	}
	public void setOrgId(java.lang.Integer orgId) {
		this.orgId = orgId;
	}
	
	public java.lang.Integer getOrgId() {
		return this.orgId;
	}
	public void setMemberAccountId(java.lang.Integer memberAccountId) {
		this.memberAccountId = memberAccountId;
	}
	
	public java.lang.Integer getMemberAccountId() {
		return this.memberAccountId;
	}
	public void setPrevValue(java.lang.Double prevValue) {
		this.prevValue = prevValue;
	}
	
	public java.lang.Double getPrevValue() {
		return this.prevValue;
	}
	public void setAfterValue(java.lang.Double afterValue) {
		this.afterValue = afterValue;
	}
	
	public java.lang.Double getAfterValue() {
		return this.afterValue;
	}
	public void setChangeValue(java.lang.Double changeValue) {
		this.changeValue = changeValue;
	}
	
	public java.lang.Double getChangeValue() {
		return this.changeValue;
	}
	public void setSysTime(java.util.Date sysTime) {
		this.sysTime = sysTime;
	}
	
	public java.util.Date getSysTime() {
		return this.sysTime;
	}
	public void setReason(java.lang.String reason) {
		this.reason = reason;
	}
	
	public java.lang.String getReason() {
		return this.reason;
	}
	public void setOperationManId(java.lang.Integer operationManId) {
		this.operationManId = operationManId;
	}
	
	public java.lang.Integer getOperationManId() {
		return this.operationManId;
	}

	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getMemberAccountLogId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(this.getMemberAccountLogId() == null){
			return false;
		}
		if(!(obj instanceof MemberAccountLog)){
			return false;
		}
		if(this == obj) {
			return true;
		}
		MemberAccountLog other = (MemberAccountLog)obj;
		return new EqualsBuilder()
			.append(getMemberAccountLogId(),other.getMemberAccountLogId())
			.isEquals();
	}
}

