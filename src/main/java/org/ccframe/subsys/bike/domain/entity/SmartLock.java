package org.ccframe.subsys.bike.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ccframe.client.Global;
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
@Table(name = "prd_smart_lock")
@AutoCacheConfig
//elasticsearch
@Document(indexName = Global.ES_DEFAULT_INDEX, type = "smartLock")
@Setting(settingPath = Global.ES_DEFAULT_ANALYSER)
public class SmartLock implements Serializable{
	
	public static final String SMART_LOCK_ID = "smartLockId";
	public static final String IMEI_CODE = "imeiCode";
	public static final String MAC_ADDRESS = "macAddress";
	public static final String LOCKER_HARDWARE_CODE = "lockerHardwareCode";
	public static final String BIKE_PLATE_NUMBER = "bikePlateNumber";
	public static final String ACTIVE_DATE = "activeDate";
	public static final String ORG_ID = "orgId";
	public static final String BIKE_TYPE_ID = "bikeTypeId";
	public static final String SMART_LOCK_STAT_CODE = "smartLockStatCode";
	public static final String LAST_USE_DATE = "lastUseDate";
	
	public static final String ACTIVE_DATE_STR = "activeDateStr";
	public static final String LAST_USE_DATE_STR = "lastUseDateStr";
	
	private static final long serialVersionUID = 1L;
	
	//columns START
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
	//elasticsearch
	@org.springframework.data.annotation.Id
	@Column(name = "SMART_LOCK_ID", nullable = false, length = 10)
	private java.lang.Integer smartLockId;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") //浣跨敤ik鍒嗚瘝鍣ㄨ繘琛屽垎璇�
	@Column(name = "IMEI_CODE", nullable = true, length = 15)
	private java.lang.String imeiCode;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") //浣跨敤ik鍒嗚瘝鍣ㄨ繘琛屽垎璇�
	@Column(name = "MAC_ADDRESS", nullable = true, length = 17)
	private java.lang.String macAddress;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") //浣跨敤ik鍒嗚瘝鍣ㄨ繘琛屽垎璇�
	@Column(name = "LOCKER_HARDWARE_CODE", nullable = false, length = 32)
	private java.lang.String lockerHardwareCode;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") //浣跨敤ik鍒嗚瘝鍣ㄨ繘琛屽垎璇�
	@Column(name = "BIKE_PLATE_NUMBER", nullable = true, length = 15)
	private java.lang.String bikePlateNumber;
	
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@Column(name = "ACTIVE_DATE", nullable = true, length = 0)
	private java.util.Date activeDate;
	
	@Column(name = "ORG_ID", nullable = false, length = 10)
	private java.lang.Integer orgId;
	
	@Column(name = "BIKE_TYPE_ID", nullable = false, length = 10)
	private java.lang.Integer bikeTypeId;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") //浣跨敤ik鍒嗚瘝鍣ㄨ繘琛屽垎璇�
	@Column(name = "SMART_LOCK_STAT_CODE", nullable = false, length = 2)
	private java.lang.String smartLockStatCode;
	
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@Column(name = "LAST_USE_DATE", nullable = true, length = 0)
	private java.util.Date lastUseDate;
	
	//columns END
	

	public SmartLock(){
	}

	//增强型方法satat
	public String getActiveDateStr() {
		return this.getActiveDate() == null ? null : UtilDateTimeClient.convertDateTimeToMmString(this.getActiveDate());
	}
	
	public void setActiveDateStr(String value) {
		if(value != null){
			this.setActiveDate(UtilDateTimeClient.convertStringToDateTime(value));
		}
	}
	
	public String getLastUseDateStr() {
		return this.getLastUseDate() == null ? null : UtilDateTimeClient.convertDateTimeToMmString(this.getLastUseDate());
	}
	
	public void setLastUseDateStr(String value) {
		if(value != null){
			this.setLastUseDate(UtilDateTimeClient.convertStringToDateTime(value));
		}
	}
	
	//增强型方法end
	public void setSmartLockId(java.lang.Integer smartLockId) {
		this.smartLockId = smartLockId;
	}
	
	public java.lang.Integer getSmartLockId() {
		return this.smartLockId;
	}
	public void setImeiCode(java.lang.String imeiCode) {
		this.imeiCode = imeiCode;
	}
	
	public java.lang.String getImeiCode() {
		return this.imeiCode;
	}
	public void setMacAddress(java.lang.String macAddress) {
		this.macAddress = macAddress;
	}
	
	public java.lang.String getMacAddress() {
		return this.macAddress;
	}
	public void setLockerHardwareCode(java.lang.String lockerHardwareCode) {
		this.lockerHardwareCode = lockerHardwareCode;
	}
	
	public java.lang.String getLockerHardwareCode() {
		return this.lockerHardwareCode;
	}
	public void setBikePlateNumber(java.lang.String bikePlateNumber) {
		this.bikePlateNumber = bikePlateNumber;
	}
	
	public java.lang.String getBikePlateNumber() {
		return this.bikePlateNumber;
	}
	public void setActiveDate(java.util.Date activeDate) {
		this.activeDate = activeDate;
	}
	
	public java.util.Date getActiveDate() {
		return this.activeDate;
	}
	public void setOrgId(java.lang.Integer orgId) {
		this.orgId = orgId;
	}
	
	public java.lang.Integer getOrgId() {
		return this.orgId;
	}
	public void setBikeTypeId(java.lang.Integer bikeTypeId) {
		this.bikeTypeId = bikeTypeId;
	}
	
	public java.lang.Integer getBikeTypeId() {
		return this.bikeTypeId;
	}
	public void setSmartLockStatCode(java.lang.String smartLockStatCode) {
		this.smartLockStatCode = smartLockStatCode;
	}
	
	public java.lang.String getSmartLockStatCode() {
		return this.smartLockStatCode;
	}
	public void setLastUseDate(java.util.Date lastUseDate) {
		this.lastUseDate = lastUseDate;
	}
	
	public java.util.Date getLastUseDate() {
		return this.lastUseDate;
	}

	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getSmartLockId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(this.getSmartLockId() == null){
			return false;
		}
		if(!(obj instanceof SmartLock)){
			return false;
		}
		if(this == obj) {
			return true;
		}
		SmartLock other = (SmartLock)obj;
		return new EqualsBuilder()
			.append(getSmartLockId(),other.getSmartLockId())
			.isEquals();
	}
}

