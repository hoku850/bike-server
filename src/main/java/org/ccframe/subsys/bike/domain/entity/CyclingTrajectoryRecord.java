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
@Table(name = "prd_cycling_trajectory_record")
@AutoCacheConfig
//elasticsearch
@Document(indexName = Global.ES_DEFAULT_INDEX, type = "cyclingTrajectoryRecord")
@Setting(settingPath = Global.ES_DEFAULT_ANALYSER)
public class CyclingTrajectoryRecord implements Serializable{
	
	public static final String CYCLING_TRAJECTORY_RECORD_ID = "cyclingTrajectoryRecordId";
	public static final String CYCLING_ORDER_ID = "cyclingOrderId";
	public static final String RECORD_LOCATION_LNG = "recordLocationLng";// 经度
	public static final String RECORD_LOCATION_LAT = "recordLocationLat"; // 纬度
	public static final String RECORD_TIME = "recordTime";
	
	public static final String RECORD_TIME_STR = "recordTimeStr";
	
	private static final long serialVersionUID = 1L;
	
	//columns START
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
	//elasticsearch
	@org.springframework.data.annotation.Id
	@Column(name = "CYCLING_TRAJECTORY_RECORD_ID", nullable = false, length = 10)
	private java.lang.Integer cyclingTrajectoryRecordId;
	
	@Column(name = "CYCLING_ORDER_ID", nullable = false, length = 10)
	private java.lang.Integer cyclingOrderId;
	
	@Column(name = "RECORD_LOCATION_LNG", nullable = false, length = 22)
	private java.lang.Double recordLocationLng;
	
	@Column(name = "RECORD_LOCATION_LAT", nullable = false, length = 22)
	private java.lang.Double recordLocationLat;
	
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@Column(name = "RECORD_TIME", nullable = false, length = 0)
	private java.util.Date recordTime;
	
	//columns END
	

	public CyclingTrajectoryRecord(){
	}

	//增强型方法satat
	public String getRecordTimeStr() {
		return this.getRecordTime() == null ? null : UtilDateTimeClient.convertDateTimeToString(this.getRecordTime());
	}
	
	public void setRecordTimeStr(String value) {
		if(value != null){
			this.setRecordTime(UtilDateTimeClient.convertStringToDateTime(value));
		}
	}
	
	//增强型方法end
	public void setCyclingTrajectoryRecordId(java.lang.Integer cyclingTrajectoryRecordId) {
		this.cyclingTrajectoryRecordId = cyclingTrajectoryRecordId;
	}
	
	public java.lang.Integer getCyclingTrajectoryRecordId() {
		return this.cyclingTrajectoryRecordId;
	}
	public void setCyclingOrderId(java.lang.Integer cyclingOrderId) {
		this.cyclingOrderId = cyclingOrderId;
	}
	
	public java.lang.Integer getCyclingOrderId() {
		return this.cyclingOrderId;
	}
	public void setRecordLocationLng(java.lang.Double recordLocationLng) {
		this.recordLocationLng = recordLocationLng;
	}
	
	public java.lang.Double getRecordLocationLng() {
		return this.recordLocationLng;
	}
	public void setRecordLocationLat(java.lang.Double recordLocationLat) {
		this.recordLocationLat = recordLocationLat;
	}
	
	public java.lang.Double getRecordLocationLat() {
		return this.recordLocationLat;
	}
	public void setRecordTime(java.util.Date recordTime) {
		this.recordTime = recordTime;
	}
	
	public java.util.Date getRecordTime() {
		return this.recordTime;
	}

	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getCyclingTrajectoryRecordId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(this.getCyclingTrajectoryRecordId() == null){
			return false;
		}
		if(!(obj instanceof CyclingTrajectoryRecord)){
			return false;
		}
		if(this == obj) {
			return true;
		}
		CyclingTrajectoryRecord other = (CyclingTrajectoryRecord)obj;
		return new EqualsBuilder()
			.append(getCyclingTrajectoryRecordId(),other.getCyclingTrajectoryRecordId())
			.isEquals();
	}
}

