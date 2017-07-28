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
@Table(name = "prd_bike_type")
@AutoCacheConfig
//elasticsearch
@Document(indexName = Global.ES_DEFAULT_INDEX, type = "bikeType")
@Setting(settingPath = Global.ES_DEFAULT_ANALYSER)
public class BikeType implements Serializable{
	
	public static final String BIKE_TYPE_ID = "bikeTypeId";
	public static final String ORG_ID = "orgId";
	public static final String BIKE_TYPE_NM = "bikeTypeNm";
	public static final String HALFHOUR_AMMOUNT = "halfhourAmmount";
	public static final String BIKE_TYPE_PICT_ID = "bikeTypePictId";
	
	
	private static final long serialVersionUID = 1L;
	
	//columns START
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
	// elasticsearch
	@org.springframework.data.annotation.Id
	@Column(name = "BIKE_TYPE_ID", nullable = false, length = 10)
	private java.lang.Integer bikeTypeId;
	
	@Column(name = "ORG_ID", nullable = false, length = 10)
	private java.lang.Integer orgId;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik")
	@Column(name = "BIKE_TYPE_NM", nullable = false, length = 15)
	private java.lang.String bikeTypeNm;
	
	@Column(name = "HALFHOUR_AMMOUNT", nullable = false, length = 10)
	private java.lang.Double halfhourAmmount;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik")
	@Column(name = "BIKE_TYPE_PICT_ID", nullable = true, length = 60)
	private java.lang.String bikeTypePictId;
	
	//columns END
	

	public BikeType(){
	}

	//增强型方法satat
	//增强型方法end
	public void setBikeTypeId(java.lang.Integer bikeTypeId) {
		this.bikeTypeId = bikeTypeId;
	}
	
	public java.lang.Integer getBikeTypeId() {
		return this.bikeTypeId;
	}
	public void setOrgId(java.lang.Integer orgId) {
		this.orgId = orgId;
	}
	
	public java.lang.Integer getOrgId() {
		return this.orgId;
	}
	public void setBikeTypeNm(java.lang.String bikeTypeNm) {
		this.bikeTypeNm = bikeTypeNm;
	}
	
	public java.lang.String getBikeTypeNm() {
		return this.bikeTypeNm;
	}
	public void setHalfhourAmmount(java.lang.Double halfhourAmmount) {
		this.halfhourAmmount = halfhourAmmount;
	}
	
	public java.lang.Double getHalfhourAmmount() {
		return this.halfhourAmmount;
	}
	public void setBikeTypePictId(java.lang.String bikeTypePictId) {
		this.bikeTypePictId = bikeTypePictId;
	}
	
	public java.lang.String getBikeTypePictId() {
		return this.bikeTypePictId;
	}

	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getBikeTypeId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(this.getBikeTypeId() == null){
			return false;
		}
		if(!(obj instanceof BikeType)){
			return false;
		}
		if(this == obj) {
			return true;
		}
		BikeType other = (BikeType)obj;
		return new EqualsBuilder()
			.append(getBikeTypeId(),other.getBikeTypeId())
			.isEquals();
	}
}

