package org.ccframe.subsys.bike.domain.entity;

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
@Table(name = "PRD_CYCLING_ORDER")
@AutoCacheConfig
//elasticsearch
@Document(indexName = "cycling_order_index", type = "cyclingOrder")
@Setting(settingPath = "elasticsearch-analyser.json")
public class CyclingOrder implements Serializable{
	
	public static final String CYCLING_ORDER_ID = "cyclingOrderId";
	public static final String USER_ID = "userId";
	public static final String ORG_ID = "orgId";
	public static final String SMART_LOCK_ID = "smartLockId";
	public static final String BIKE_PLATE_NUMBER = "bikePlateNumber";
	public static final String START_TIME = "startTime";
	public static final String START_LOCATION_LNG = "startLocationLng";
	public static final String START_LOCATION_LAT = "startLocationLat";
	public static final String END_TIME = "endTime";
	public static final String END_LOCATION_LNG = "endLocationLng";
	public static final String END_LOCATION_LAT = "endLocationLat";
	public static final String CYCLING_ORDER_STAT_CODE = "cyclingOrderStatCode";
	public static final String CYCLING_CONTINOUS_SEC = "cyclingContinousSec";
	public static final String CYCLING_DISTANCE_METER = "cyclingDistanceMeter";
	public static final String ORDER_AMMOUNT = "orderAmmount";
	
	public static final String START_TIME_STR = "startTimeStr";
	public static final String END_TIME_STR = "endTimeStr";
	
	private static final long serialVersionUID = 1L;
	
	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//elasticsearch
	@org.springframework.data.annotation.Id
	@Column(name = "CYCLING_ORDER_ID", nullable = false, length = 10)
	private java.lang.Integer cyclingOrderId;
	
	@Column(name = "USER_ID", nullable = false, length = 10)
	private java.lang.Integer userId;
	
	@Column(name = "ORG_ID", nullable = false, length = 10)
	private java.lang.Integer orgId;
	
	@Column(name = "SMART_LOCK_ID", nullable = false, length = 10)
	private java.lang.Integer smartLockId;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") 
	@Column(name = "BIKE_PLATE_NUMBER", nullable = false, length = 15)
	private java.lang.String bikePlateNumber;
	
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@Column(name = "START_TIME", nullable = false, length = 0)
	private java.util.Date startTime;
	
	@Column(name = "START_LOCATION_LNG", nullable = false, length = 22)
	private java.lang.Double startLocationLng;
	
	@Column(name = "START_LOCATION_LAT", nullable = false, length = 22)
	private java.lang.Double startLocationLat;
	
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@Column(name = "END_TIME", nullable = true, length = 0)
	private java.util.Date endTime;
	
	@Column(name = "END_LOCATION_LNG", nullable = true, length = 22)
	private java.lang.Double endLocationLng;
	
	@Column(name = "END_LOCATION_LAT", nullable = true, length = 22)
	private java.lang.Double endLocationLat;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik")
	@Column(name = "CYCLING_ORDER_STAT_CODE", nullable = false, length = 2)
	private java.lang.String cyclingOrderStatCode;
	
	@Column(name = "CYCLING_CONTINOUS_SEC", nullable = false, length = 10)
	private java.lang.Integer cyclingContinousSec;
	
	@Column(name = "CYCLING_DISTANCE_METER", nullable = false, length = 10)
	private java.lang.Integer cyclingDistanceMeter;
	
	@Column(name = "ORDER_AMMOUNT", nullable = false, length = 22)
	private java.lang.Double orderAmmount;
	
	//columns END
	

	public CyclingOrder(){
	}

	//增强型方法satat
	public String getStartTimeStr() {
		return this.getStartTime() == null ? null : UtilDateTimeClient.convertDateTimeToMmString(this.getStartTime());
	}
	
	public void setStartTimeStr(String value) {
		if(value != null){
			this.setStartTime(UtilDateTimeClient.convertStringToDateTime(value));
		}
	}
	
	public String getEndTimeStr() {
		return this.getEndTime() == null ? null : UtilDateTimeClient.convertDateTimeToMmString(this.getEndTime());
	}
	
	public void setEndTimeStr(String value) {
		if(value != null){
			this.setEndTime(UtilDateTimeClient.convertStringToDateTime(value));
		}
	}
	
	//增强型方法end
	public void setCyclingOrderId(java.lang.Integer cyclingOrderId) {
		this.cyclingOrderId = cyclingOrderId;
	}
	
	public java.lang.Integer getCyclingOrderId() {
		return this.cyclingOrderId;
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
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}
	
	public java.util.Date getStartTime() {
		return this.startTime;
	}
	public void setStartLocationLng(java.lang.Double startLocationLng) {
		this.startLocationLng = startLocationLng;
	}
	
	public java.lang.Double getStartLocationLng() {
		return this.startLocationLng;
	}
	public void setStartLocationLat(java.lang.Double startLocationLat) {
		this.startLocationLat = startLocationLat;
	}
	
	public java.lang.Double getStartLocationLat() {
		return this.startLocationLat;
	}
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}
	
	public java.util.Date getEndTime() {
		return this.endTime;
	}
	public void setEndLocationLng(java.lang.Double endLocationLng) {
		this.endLocationLng = endLocationLng;
	}
	
	public java.lang.Double getEndLocationLng() {
		return this.endLocationLng;
	}
	public void setEndLocationLat(java.lang.Double endLocationLat) {
		this.endLocationLat = endLocationLat;
	}
	
	public java.lang.Double getEndLocationLat() {
		return this.endLocationLat;
	}
	public void setCyclingOrderStatCode(java.lang.String cyclingOrderStatCode) {
		this.cyclingOrderStatCode = cyclingOrderStatCode;
	}
	
	public java.lang.String getCyclingOrderStatCode() {
		return this.cyclingOrderStatCode;
	}
	public void setCyclingContinousSec(java.lang.Integer cyclingContinousSec) {
		this.cyclingContinousSec = cyclingContinousSec;
	}
	
	public java.lang.Integer getCyclingContinousSec() {
		return this.cyclingContinousSec;
	}
	public void setCyclingDistanceMeter(java.lang.Integer cyclingDistanceMeter) {
		this.cyclingDistanceMeter = cyclingDistanceMeter;
	}
	
	public java.lang.Integer getCyclingDistanceMeter() {
		return this.cyclingDistanceMeter;
	}
	public void setOrderAmmount(java.lang.Double orderAmmount) {
		this.orderAmmount = orderAmmount;
	}
	
	public java.lang.Double getOrderAmmount() {
		return this.orderAmmount;
	}

	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getCyclingOrderId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(this.getCyclingOrderId() == null){
			return false;
		}
		if(!(obj instanceof CyclingOrder)){
			return false;
		}
		if(this == obj) {
			return true;
		}
		CyclingOrder other = (CyclingOrder)obj;
		return new EqualsBuilder()
			.append(getCyclingOrderId(),other.getCyclingOrderId())
			.isEquals();
	}
}

