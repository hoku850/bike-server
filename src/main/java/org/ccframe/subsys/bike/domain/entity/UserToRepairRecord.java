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
@Table(name = "prd_user_to_repair_record")
@AutoCacheConfig
//elasticsearch
@Document(indexName = Global.ES_DEFAULT_INDEX, type = "userToRepairRecord")
@Setting(settingPath = Global.ES_DEFAULT_ANALYSER)
public class UserToRepairRecord implements Serializable{
	
	public static final String USER_TO_REPAIR_RECORD_ID = "userToRepairRecordId";
	public static final String SMART_LOCK_ID = "60006000";
	public static final String BIKE_PLATE_NUMBER = "bikePlateNumber";
	public static final String USER_ID = "userId";
	public static final String ORG_ID = "orgId";
	public static final String TO_REPAIR_TIME = "toRepairTime";
	public static final String TO_REPAIR_LOCATION_LNG = "toRepairLocationLng";
	public static final String TO_REPAIR_LOCATION_LAT = "toRepairLocationLat";
	public static final String TO_REPAIR_LOCATION_CODE = "toRepairLocationCode";
	public static final String TO_REPAIR_REASON_ID = "toRepairReasonId";
	public static final String IF_FINISH_FIX = "ifFinishFix";
	public static final String FINISH_FIX_TIME = "finishFixTime";
	
	public static final String TO_REPAIR_TIME_STR = "toRepairTimeStr";
	public static final String FINISH_FIX_TIME_STR = "finishFixTimeStr";
	
	private static final long serialVersionUID = 1L;
	
	//columns START
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
	//elasticsearch
	@org.springframework.data.annotation.Id
	@Field(type = FieldType.Integer, analyzer="ik")
	@Column(name = "USER_TO_REPAIR_RECORD_ID", nullable = false, length = 10)
	private java.lang.Integer userToRepairRecordId;
	
	@Column(name = "SMART_LOCK_ID", nullable = true, length = 10)
	private java.lang.Integer smartLockId;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") //浣跨敤ik鍒嗚瘝鍣ㄨ繘琛屽垎璇�
	@Column(name = "BIKE_PLATE_NUMBER", nullable = false, length = 15)
	private java.lang.String bikePlateNumber;
	
	@Column(name = "USER_ID", nullable = true, length = 10)
	private java.lang.Integer userId;
	
	@Column(name = "ORG_ID", nullable = true, length = 10)
	private java.lang.Integer orgId;
	
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@Column(name = "TO_REPAIR_TIME", nullable = true, length = 0)
	private java.util.Date toRepairTime;
	
	@Column(name = "TO_REPAIR_LOCATION_LNG", nullable = true, length = 22)
	private java.lang.Double toRepairLocationLng;
	
	@Column(name = "TO_REPAIR_LOCATION_LAT", nullable = true, length = 22)
	private java.lang.Double toRepairLocationLat;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") //浣跨敤ik鍒嗚瘝鍣ㄨ繘琛屽垎璇�
	@Column(name = "TO_REPAIR_LOCATION_CODE", nullable = true, length = 2)
	private java.lang.String toRepairLocationCode;
	
	@Column(name = "TO_REPAIR_REASON_ID", nullable = true, length = 10)
	private java.lang.Integer toRepairReasonId;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") //浣跨敤ik鍒嗚瘝鍣ㄨ繘琛屽垎璇�
	@Column(name = "IF_FINISH_FIX", nullable = false, length = 2)
	private java.lang.String ifFinishFix;
	
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@Column(name = "FINISH_FIX_TIME", nullable = true, length = 0)
	private java.util.Date finishFixTime;
	
	//columns END
	

	public UserToRepairRecord(){
	}

	//增强型方法satat
	public String getToRepairTimeStr() {
		return this.getToRepairTime() == null ? null : UtilDateTimeClient.convertDateTimeToMmString(this.getToRepairTime());
	}
	
	public void setToRepairTimeStr(String value) {
		if(value != null){
			this.setToRepairTime(UtilDateTimeClient.convertStringToDateTime(value));
		}
	}
	
	public String getFinishFixTimeStr() {
		return this.getFinishFixTime() == null ? null : UtilDateTimeClient.convertDateTimeToMmString(this.getFinishFixTime());
	}
	
	public void setFinishFixTimeStr(String value) {
		if(value != null){
			this.setFinishFixTime(UtilDateTimeClient.convertStringToDateTime(value));
		}
	}
	
	//增强型方法end
	public void setUserToRepairRecordId(java.lang.Integer userToRepairRecordId) {
		this.userToRepairRecordId = userToRepairRecordId;
	}
	
	public java.lang.Integer getUserToRepairRecordId() {
		return this.userToRepairRecordId;
	}
	public void setSmartLockId(java.lang.Integer smartLockId) {
		this.smartLockId = smartLockId;
	}
	
	public java.lang.Integer getSmartLockId() {
		return this.smartLockId;
	}
	public void setBikePlateNumber(java.lang.String bikePlateNumber) {
		this.bikePlateNumber = bikePlateNumber;
	}
	
	public java.lang.String getBikePlateNumber() {
		return this.bikePlateNumber;
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
	public void setToRepairTime(java.util.Date toRepairTime) {
		this.toRepairTime = toRepairTime;
	}
	
	public java.util.Date getToRepairTime() {
		return this.toRepairTime;
	}
	public void setToRepairLocationLng(java.lang.Double toRepairLocationLng) {
		this.toRepairLocationLng = toRepairLocationLng;
	}
	
	public java.lang.Double getToRepairLocationLng() {
		return this.toRepairLocationLng;
	}
	public void setToRepairLocationLat(java.lang.Double toRepairLocationLat) {
		this.toRepairLocationLat = toRepairLocationLat;
	}
	
	public java.lang.Double getToRepairLocationLat() {
		return this.toRepairLocationLat;
	}

	

	public java.lang.String getToRepairLocationCode() {
		return toRepairLocationCode;
	}

	public void setToRepairLocationCode(java.lang.String toRepairLocationCode) {
		this.toRepairLocationCode = toRepairLocationCode;
	}

	public java.lang.Integer getToRepairReasonId() {
		return toRepairReasonId;
	}

	public void setToRepairReasonId(java.lang.Integer toRepairReasonId) {
		this.toRepairReasonId = toRepairReasonId;
	}

	public void setIfFinishFix(java.lang.String ifFinishFix) {
		this.ifFinishFix = ifFinishFix;
	}
	
	public java.lang.String getIfFinishFix() {
		return this.ifFinishFix;
	}
	public void setFinishFixTime(java.util.Date finishFixTime) {
		this.finishFixTime = finishFixTime;
	}
	
	public java.util.Date getFinishFixTime() {
		return this.finishFixTime;
	}

	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getUserToRepairRecordId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(this.getUserToRepairRecordId() == null){
			return false;
		}
		if(!(obj instanceof UserToRepairRecord)){
			return false;
		}
		if(this == obj) {
			return true;
		}
		UserToRepairRecord other = (UserToRepairRecord)obj;
		return new EqualsBuilder()
			.append(getUserToRepairRecordId(),other.getUserToRepairRecordId())
			.isEquals();
	}
}

