package org.ccframe.subsys.bike.domain.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.ccframe.client.commons.UtilDateTimeClient;







import org.ccframe.commons.cache.AutoCacheConfig;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.ccframe.subsys.bike.domain.*;

@Entity
@Table(name = "PRD_SMART_LOCK_STAT")
@AutoCacheConfig
//elasticsearch
@Document(indexName = "smart_lock_stat_index", type = "smartLockStat")
@Setting(settingPath = "elasticsearch-analyser.json")
public class SmartLockStat implements Serializable{
	
	public static final String SMART_LOCK_STAT_ID = "smartLockStatId";
	public static final String SMART_LOCK_ID = "smartLockId";
	public static final String ORG_ID = "orgId";
	public static final String LOCK_LNG = "lockLng";
	public static final String LOCK_LAT = "lockLat";
	public static final String LOCK_BATTERY = "lockBattery";
	public static final String LOCK_SWITCH_STAT_CODE = "lockSwitchStatCode";
	public static final String IF_REPAIR_ING = "ifRepairIng";
	public static final String LAST_LOCATION_UPD_TIME = "lastLocationUpdTime";
	
	
	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return "SmartLockStat [smartLockStatId=" + smartLockStatId
				+ ", smartLockId=" + smartLockId + ", orgId=" + orgId
				+ ", lockBattery=" + lockBattery + ", lockLng=" + lockLng
				+ ", lockLat=" + lockLat + ", ifRepairIng=" + ifRepairIng
				+ ", lockSwitchStatCode=" + lockSwitchStatCode
				+ ", lastLocationUpdTime=" + lastLocationUpdTime + "]";
	}

	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//elasticsearch
	@org.springframework.data.annotation.Id
	@Column(name = "PRD_SMART_LOCK_STAT_ID", nullable = false, length = 10)
	private java.lang.Integer smartLockStatId;
	
	@Column(name = "SMART_LOCK_ID", nullable = true, length = 10)
	private java.lang.Integer smartLockId;
	
	@Column(name = "ORG_ID", nullable = true, length = 10)
	private java.lang.Integer orgId;
	
	@Column(name = "LOCK_BATTERY", nullable = true, length = 10)
	private java.lang.Integer lockBattery;
	
	@Column(name = "LOCK_LNG", nullable = true, length = 22)
	private java.lang.Double lockLng;
	
	@Column(name = "LOCK_LAT", nullable = true, length = 22)
	private java.lang.Double lockLat;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") //浣跨敤ik鍒嗚瘝鍣ㄨ繘琛屽垎璇�
	@Column(name = "IF_REPAIR_ING", nullable = true, length = 2)
	private java.lang.String ifRepairIng;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") //浣跨敤ik鍒嗚瘝鍣ㄨ繘琛屽垎璇�
	@Column(name = "LOCK_SWITCH_STAT_CODE", nullable = true, length = 2)
	private java.lang.String lockSwitchStatCode;
	
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@Column(name = "LAST_LOCATION_UPD_TIME", nullable = true, length = 0)
	private java.util.Date lastLocationUpdTime;
	
	//columns END
	

	public SmartLockStat(){
	}
	
	
	
	public java.lang.String getLockSwitchStatCode() {
		return lockSwitchStatCode;
	}



	public void setLockSwitchStatCode(java.lang.String lockSwitchStatCode) {
		this.lockSwitchStatCode = lockSwitchStatCode;
	}



	public java.lang.Integer getSmartLockStatId() {
		return smartLockStatId;
	}



	public void setSmartLockStatId(java.lang.Integer smartLockStatId) {
		this.smartLockStatId = smartLockStatId;
	}



	public java.lang.Integer getSmartLockId() {
		return smartLockId;
	}



	public void setSmartLockId(java.lang.Integer smartLockId) {
		this.smartLockId = smartLockId;
	}



	public java.lang.Integer getOrgId() {
		return orgId;
	}



	public void setOrgId(java.lang.Integer orgId) {
		this.orgId = orgId;
	}



	public java.lang.Integer getLockBattery() {
		return lockBattery;
	}



	public void setLockBattery(java.lang.Integer lockBattery) {
		this.lockBattery = lockBattery;
	}



	public java.lang.Double getLockLng() {
		return lockLng;
	}



	public void setLockLng(java.lang.Double lockLng) {
		this.lockLng = lockLng;
	}



	public java.lang.Double getLockLat() {
		return lockLat;
	}



	public void setLockLat(java.lang.Double lockLat) {
		this.lockLat = lockLat;
	}



	public java.lang.String getIfRepairIng() {
		return ifRepairIng;
	}



	public void setIfRepairIng(java.lang.String ifRepairIng) {
		this.ifRepairIng = ifRepairIng;
	}



	public java.util.Date getLastLocationUpdTime() {
		return lastLocationUpdTime;
	}



	public void setLastLocationUpdTime(java.util.Date lastLocationUpdTime) {
		this.lastLocationUpdTime = lastLocationUpdTime;
	}



	//增强型方法end
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSmartLockStatId())
			.toHashCode();
	}

	public boolean equals(Object obj) {
		if(this.getSmartLockStatId() == null){
			return false;
		}
		if(!(obj instanceof SmartLockStat)){
			return false;
		}
		if(this == obj) {
			return true;
		}
		SmartLockStat other = (SmartLockStat)obj;
		return new EqualsBuilder()
			.append(getSmartLockStatId(),other.getSmartLockStatId())
			.isEquals();
	}
}

