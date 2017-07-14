package org.ccframe.subsys.core.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ccframe.commons.cache.AutoCacheConfig;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

@Entity
@Table(name = "SYS_MEMBER_ACCOUNT")
@AutoCacheConfig
//elasticsearch
@Document(indexName = "member_account_index", type = "memberAccount")
@Setting(settingPath = "elasticsearch-analyser.json")
public class MemberAccount implements Serializable{
	
	public static final String MEMBER_ACCOUNT_ID = "memberAccountId";
	public static final String USER_ID = "userId";
	public static final String ORG_ID = "orgId";
	public static final String ACCOUNT_TYPE_CODE = "accountTypeCode";
	public static final String ACCOUNT_VALUE = "accountValue";
	
	
	private static final long serialVersionUID = 1L;
	
	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//elasticsearch
	@org.springframework.data.annotation.Id
	@Column(name = "MEMBER_ACCOUNT_ID", nullable = false, length = 10)
	private java.lang.Integer memberAccountId;
	
	@Column(name = "USER_ID", nullable = false, length = 10)
	private java.lang.Integer userId;
	
	@Column(name = "ORG_ID", nullable = true, length = 10)
	private java.lang.Integer orgId;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") 
	@Column(name = "ACCOUNT_TYPE_CODE", nullable = false, length = 2)
	private java.lang.String accountTypeCode;
	
	@Column(name = "ACCOUNT_VALUE", nullable = false, length = 22)
	private java.lang.Double accountValue;
	
	//columns END
	

	public MemberAccount(){
	}

	//增强型方法satat
	//增强型方法end
	public void setMemberAccountId(java.lang.Integer memberAccountId) {
		this.memberAccountId = memberAccountId;
	}
	
	public java.lang.Integer getMemberAccountId() {
		return this.memberAccountId;
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
	public void setAccountTypeCode(java.lang.String accountTypeCode) {
		this.accountTypeCode = accountTypeCode;
	}
	
	public java.lang.String getAccountTypeCode() {
		return this.accountTypeCode;
	}
	public void setAccountValue(java.lang.Double accountValue) {
		this.accountValue = accountValue;
	}
	
	public java.lang.Double getAccountValue() {
		return this.accountValue;
	}

	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getMemberAccountId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(this.getMemberAccountId() == null){
			return false;
		}
		if(!(obj instanceof MemberAccount)){
			return false;
		}
		if(this == obj) {
			return true;
		}
		MemberAccount other = (MemberAccount)obj;
		return new EqualsBuilder()
			.append(getMemberAccountId(),other.getMemberAccountId())
			.isEquals();
	}
}

