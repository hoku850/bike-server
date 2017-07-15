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
@Table(name = "PRD_TO_REPAIR_REASON")
@AutoCacheConfig
//elasticsearch
@Document(indexName = "bike_locked_stat_index", type = "bikeLockedStat")
@Setting(settingPath = "elasticsearch-analyser.json")
public class ToRepairReason implements Serializable{
	
	public static final String TO_REPAIR_REASON_ID = "toRepairReasonId";
	public static final String TO_REPAIR_LOCATION_CODE = "toRepairLocationCode";
	public static final String TO_REPAIR_REASON_CONT = "toRepairReasonCont";
	
	private static final long serialVersionUID = 1L;
	
	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//elasticsearch
	@org.springframework.data.annotation.Id
	@Column(name = "TO_REPAIR_REASON_ID", nullable = false, length = 10)
	private java.lang.Integer toRepairReasonId;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") 
	@Column(name = "TO_REPAIR_LOCATION_CODE", nullable = false, length = 15)
	private java.lang.String toRepairLocationCode;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") 
	@Column(name = "TO_REPAIR_REASON_CONT", nullable = false, length = 15)
	private java.lang.String toRepairReasonCont;
	
	//columns END
	

	public ToRepairReason(){
	}
	
	//增强型方法end
	

	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getToRepairReasonId())
			.toHashCode();
	}
	
	public java.lang.Integer getToRepairReasonId() {
		return toRepairReasonId;
	}

	public void setToRepairReasonId(java.lang.Integer toRepairReasonId) {
		this.toRepairReasonId = toRepairReasonId;
	}

	public java.lang.String getToRepairLocationCode() {
		return toRepairLocationCode;
	}

	public void setToRepairLocationCode(java.lang.String toRepairLocationCode) {
		this.toRepairLocationCode = toRepairLocationCode;
	}

	public java.lang.String getToRepairReasonCont() {
		return toRepairReasonCont;
	}

	public void setToRepairReasonCont(java.lang.String toRepairReasonCont) {
		this.toRepairReasonCont = toRepairReasonCont;
	}

	public boolean equals(Object obj) {
		if(this.getToRepairReasonId() == null){
			return false;
		}
		if(!(obj instanceof ToRepairReason)){
			return false;
		}
		if(this == obj) {
			return true;
		}
		ToRepairReason other = (ToRepairReason)obj;
		return new EqualsBuilder()
			.append(getToRepairReasonId(),other.getToRepairReasonId())
			.isEquals();
	}
}
