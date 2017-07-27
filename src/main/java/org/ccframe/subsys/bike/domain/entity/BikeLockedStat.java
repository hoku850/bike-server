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
@Table(name = "prd_bike_locked_stat")
@AutoCacheConfig
//elasticsearch
@Document(indexName = Global.ES_DEFAULT_INDEX, type = "bikeLockedStat")
@Setting(settingPath = Global.ES_DEFAULT_ANALYSER)
public class BikeLockedStat implements Serializable{
	
	public static final String BIKE_LOCKED_STAT_ID = "bikeLockedStatId";
	public static final String SMART_LOCK_ID = "smartLockId";
	public static final String USER_ID = "userId";
	public static final String LOCKED_START_TIME = "lockedStartTime";
	public static final String LOCKED_END_TIME = "lockedEndTime";
	
	public static final String LOCKED_START_TIME_STR = "lockedStartTimeStr";
	public static final String LOCKED_END_TIME_STR = "lockedEndTimeStr";
	
	private static final long serialVersionUID = 1L;
	
	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//elasticsearch
	@org.springframework.data.annotation.Id
	@Column(name = "BIKE_LOCKED_STAT_ID", nullable = false, length = 10)
	private java.lang.Integer bikeLockedStatId;
	
	@Column(name = "SMART_LOCK_ID", nullable = false, length = 10)
	private java.lang.Integer smartLockId;
	
	@Column(name = "USER_ID", nullable = false, length = 10)
	private java.lang.Integer userId;
	
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@Column(name = "LOCKED_START_TIME", nullable = false, length = 0)
	private java.util.Date lockedStartTime;
	
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@Column(name = "LOCKED_END_TIME", nullable = false, length = 0)
	private java.util.Date lockedEndTime;
	
	//columns END
	

	public BikeLockedStat(){
	}

	//增强型方法satat
	public String getLockedStartTimeStr() {
		return this.getLockedStartTime() == null ? null : UtilDateTimeClient.convertDateTimeToString(this.getLockedStartTime());
	}
	
	public void setLockedStartTimeStr(String value) {
		if(value != null){
			this.setLockedStartTime(UtilDateTimeClient.convertStringToDateTime(value));
		}
	}
	
	public String getLockedEndTimeStr() {
		return this.getLockedEndTime() == null ? null : UtilDateTimeClient.convertDateTimeToString(this.getLockedEndTime());
	}
	
	public void setLockedEndTimeStr(String value) {
		if(value != null){
			this.setLockedEndTime(UtilDateTimeClient.convertStringToDateTime(value));
		}
	}
	
	//增强型方法end
	public void setBikeLockedStatId(java.lang.Integer bikeLockedStatId) {
		this.bikeLockedStatId = bikeLockedStatId;
	}
	
	public java.lang.Integer getBikeLockedStatId() {
		return this.bikeLockedStatId;
	}
	public void setSmartLockId(java.lang.Integer smartLockId) {
		this.smartLockId = smartLockId;
	}
	
	public java.lang.Integer getSmartLockId() {
		return this.smartLockId;
	}
	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}
	
	public java.lang.Integer getUserId() {
		return this.userId;
	}
	public void setLockedStartTime(java.util.Date lockedStartTime) {
		this.lockedStartTime = lockedStartTime;
	}
	
	public java.util.Date getLockedStartTime() {
		return this.lockedStartTime;
	}
	public void setLockedEndTime(java.util.Date lockedEndTime) {
		this.lockedEndTime = lockedEndTime;
	}
	
	public java.util.Date getLockedEndTime() {
		return this.lockedEndTime;
	}

	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getBikeLockedStatId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(this.getBikeLockedStatId() == null){
			return false;
		}
		if(!(obj instanceof BikeLockedStat)){
			return false;
		}
		if(this == obj) {
			return true;
		}
		BikeLockedStat other = (BikeLockedStat)obj;
		return new EqualsBuilder()
			.append(getBikeLockedStatId(),other.getBikeLockedStatId())
			.isEquals();
	}
}

