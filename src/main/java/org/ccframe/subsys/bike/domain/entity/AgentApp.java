package org.ccframe.subsys.bike.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ccframe.client.Global;
import org.ccframe.commons.cache.AutoCacheConfig;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

@Entity
@Table(name = "prd_agent_app")
@AutoCacheConfig
//elasticsearch
@Document(indexName = Global.ES_DEFAULT_INDEX, type = "agentApp")
@Setting(settingPath = Global.ES_DEFAULT_ANALYSER)
public class AgentApp implements Serializable{
	
	public static final String AGENT_APP_ID = "agentAppId";
	public static final String APP_NM = "appNm";
	public static final String CHARGE_DEPOSIT = "chargeDeposit";
	public static final String ORG_ID = "orgId";
	public static final String IOS_URL = "iosUrl";
	public static final String ANDROID_URL = "androidUrl";
	public static final String AGENT_ICON_ID = "agentIconId";
	
	
	private static final long serialVersionUID = 1L;
	
	//columns START
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
	//elasticsearch
	@org.springframework.data.annotation.Id
//	@Field(type = FieldType.Integer, analyzer="ik")
	@Column(name = "AGENT_APP_ID", nullable = false, length = 10)
	private java.lang.Integer agentAppId;
	
	//elasticsearch 
//	@Field(type = FieldType.String, analyzer="ik") //浣跨敤ik鍒嗚瘝鍣ㄨ繘琛屽垎璇�
	@Column(name = "APP_NM", nullable = false, length = 10)
	private java.lang.String appNm;
	
	@Column(name = "CHARGE_DEPOSIT", nullable = true, length = 22)
	private java.lang.Double chargeDeposit;
	
	@Column(name = "ORG_ID", nullable = false, length = 10)
	private java.lang.Integer orgId;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") //浣跨敤ik鍒嗚瘝鍣ㄨ繘琛屽垎璇�
	@Column(name = "IOS_URL", nullable = true, length = 256)
	private java.lang.String iosUrl;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") //浣跨敤ik鍒嗚瘝鍣ㄨ繘琛屽垎璇�
	@Column(name = "ANDROID_URL", nullable = true, length = 256)
	private java.lang.String androidUrl;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") //浣跨敤ik鍒嗚瘝鍣ㄨ繘琛屽垎璇�
	@Column(name = "AGENT_ICON_ID", nullable = true, length = 60)
	private java.lang.String agentIconId;
	
	//columns END
	

	public AgentApp(){
	}

	//增强型方法satat
	//增强型方法end
	public void setAgentAppId(java.lang.Integer agentAppId) {
		this.agentAppId = agentAppId;
	}
	
	public java.lang.Integer getAgentAppId() {
		return this.agentAppId;
	}
	public void setAppNm(java.lang.String appNm) {
		this.appNm = appNm;
	}
	
	public java.lang.String getAppNm() {
		return this.appNm;
	}
	public void setChargeDeposit(java.lang.Double chargeDeposit) {
		this.chargeDeposit = chargeDeposit;
	}
	
	public java.lang.Double getChargeDeposit() {
		return this.chargeDeposit;
	}
	public void setOrgId(java.lang.Integer orgId) {
		this.orgId = orgId;
	}
	
	public java.lang.Integer getOrgId() {
		return this.orgId;
	}
	
	
	
	public java.lang.String getIosUrl() {
		return iosUrl;
	}

	public void setIosUrl(java.lang.String iosUrl) {
		this.iosUrl = iosUrl;
	}

	public void setAndroidUrl(java.lang.String androidUrl) {
		this.androidUrl = androidUrl;
	}
	
	public java.lang.String getAndroidUrl() {
		return this.androidUrl;
	}
	public void setAgentIconId(java.lang.String agentIconId) {
		this.agentIconId = agentIconId;
	}
	
	public java.lang.String getAgentIconId() {
		return this.agentIconId;
	}

	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getAgentAppId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(this.getAgentAppId() == null){
			return false;
		}
		if(!(obj instanceof AgentApp)){
			return false;
		}
		if(this == obj) {
			return true;
		}
		AgentApp other = (AgentApp)obj;
		return new EqualsBuilder()
			.append(getAgentAppId(),other.getAgentAppId())
			.isEquals();
	}
}

