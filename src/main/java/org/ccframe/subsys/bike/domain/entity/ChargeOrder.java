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
@Table(name = "prd_charge_order")
@AutoCacheConfig
//elasticsearch
@Document(indexName = "charge_order_index", type = "chargeOrder")
@Setting(settingPath = "elasticsearch-analyser.json")
public class ChargeOrder implements Serializable{
	
	public static final String CHARGE_ORDER_ID = "chargeOrderId";
	public static final String CHARGE_ORDER_NUM = "chargeOrderNum";
	public static final String USER_ID = "userId";
	public static final String MEMBER_ACCOUNT_ID = "memberAccountId";
	public static final String MEMBER_ACCOUNT_LOG_ID = "memberAccountLogId";
	public static final String ORG_ID = "orgId";
	public static final String PAYMENT_TYPE_CODE = "paymentTypeCode";
	public static final String PAYMENT_TRANSACTIONAL_NUMBER = "paymentTransactionalNumber";
	public static final String CHARGE_AMMOUNT = "chargeAmmount";
	public static final String CHARGE_ORDER_STAT_CODE = "chargeOrderStatCode";
	public static final String CREATE_TIME = "createTime";
	public static final String CHARGE_FINISH_TIME = "chargeFinishTime";
	public static final String REFUND_FINISH_TIME = "refundFinishTime";
	
	public static final String CREATE_TIME_STR = "createTimeStr";
	public static final String CHARGE_FINISH_TIME_STR = "chargeFinishTimeStr";
	public static final String REFUND_FINISH_TIME_STR = "refundFinishTimeStr";
	
	private static final long serialVersionUID = 1L;
	
	//columns START
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//elasticsearch
	@org.springframework.data.annotation.Id
	@Column(name = "CHARGE_ORDER_ID", nullable = false, length = 10)
	private java.lang.Integer chargeOrderId;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") 
	@Column(name = "CHARGE_ORDER_NUM", nullable = false, length = 16)
	private java.lang.String chargeOrderNum;
	
	@Column(name = "USER_ID", nullable = false, length = 10)
	private java.lang.Integer userId;
	
	@Column(name = "MEMBER_ACCOUNT_ID", nullable = false, length = 10)
	private java.lang.Integer memberAccountId;
	
	@Column(name = "MEMBER_ACCOUNT_LOG_ID", nullable = true, length = 10)
	private java.lang.Integer memberAccountLogId;
	
	@Column(name = "ORG_ID", nullable = false, length = 10)
	private java.lang.Integer orgId;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") 
	@Column(name = "PAYMENT_TYPE_CODE", nullable = false, length = 2)
	private java.lang.String paymentTypeCode;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") 
	@Column(name = "PAYMENT_TRANSACTIONAL_NUMBER", nullable = true, length = 64)
	private java.lang.String paymentTransactionalNumber;
	
	@Column(name = "CHARGE_AMMOUNT", nullable = false, length = 22)
	private java.lang.Double chargeAmmount;
	
	//elasticsearch 
	//@Field(type = FieldType.String, analyzer="ik") 
	@Column(name = "CHARGE_ORDER_STAT_CODE", nullable = false, length = 2)
	private java.lang.String chargeOrderStatCode;
	
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@Column(name = "CREATE_TIME", nullable = false, length = 0)
	private java.util.Date createTime;
	
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@Column(name = "CHARGE_FINISH_TIME", nullable = true, length = 0)
	private java.util.Date chargeFinishTime;
	
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@Column(name = "REFUND_FINISH_TIME", nullable = true, length = 0)
	private java.util.Date refundFinishTime;
	
	//columns END
	

	public ChargeOrder(){
	}

	//增强型方法satat
	public String getCreateTimeStr() {
		return this.getCreateTime() == null ? null : UtilDateTimeClient.convertDateTimeToString(this.getCreateTime());
	}
	
	public void setCreateTimeStr(String value) {
		if(value != null){
			this.setCreateTime(UtilDateTimeClient.convertStringToDateTime(value));
		}
	}
	
	public String getChargeFinishTimeStr() {
		return this.getChargeFinishTime() == null ? null : UtilDateTimeClient.convertDateTimeToString(this.getChargeFinishTime());
	}
	
	public void setChargeFinishTimeStr(String value) {
		if(value != null){
			this.setChargeFinishTime(UtilDateTimeClient.convertStringToDateTime(value));
		}
	}
	
	public String getRefundFinishTimeStr() {
		return this.getRefundFinishTime() == null ? null : UtilDateTimeClient.convertDateTimeToString(this.getRefundFinishTime());
	}
	
	public void setRefundFinishTimeStr(String value) {
		if(value != null){
			this.setRefundFinishTime(UtilDateTimeClient.convertStringToDateTime(value));
		}
	}
	
	//增强型方法end
	public void setChargeOrderId(java.lang.Integer chargeOrderId) {
		this.chargeOrderId = chargeOrderId;
	}
	
	public java.lang.Integer getChargeOrderId() {
		return this.chargeOrderId;
	}
	public void setChargeOrderNum(java.lang.String chargeOrderNum) {
		this.chargeOrderNum = chargeOrderNum;
	}
	
	public java.lang.String getChargeOrderNum() {
		return this.chargeOrderNum;
	}
	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}
	
	public java.lang.Integer getUserId() {
		return this.userId;
	}
	public void setMemberAccountId(java.lang.Integer memberAccountId) {
		this.memberAccountId = memberAccountId;
	}
	
	public java.lang.Integer getMemberAccountId() {
		return this.memberAccountId;
	}
	public void setMemberAccountLogId(java.lang.Integer memberAccountLogId) {
		this.memberAccountLogId = memberAccountLogId;
	}
	
	public java.lang.Integer getMemberAccountLogId() {
		return this.memberAccountLogId;
	}
	public void setOrgId(java.lang.Integer orgId) {
		this.orgId = orgId;
	}
	
	public java.lang.Integer getOrgId() {
		return this.orgId;
	}
	public void setPaymentTypeCode(java.lang.String paymentTypeCode) {
		this.paymentTypeCode = paymentTypeCode;
	}
	
	public java.lang.String getPaymentTypeCode() {
		return this.paymentTypeCode;
	}
	public void setPaymentTransactionalNumber(java.lang.String paymentTransactionalNumber) {
		this.paymentTransactionalNumber = paymentTransactionalNumber;
	}
	
	public java.lang.String getPaymentTransactionalNumber() {
		return this.paymentTransactionalNumber;
	}
	public void setChargeAmmount(java.lang.Double chargeAmmount) {
		this.chargeAmmount = chargeAmmount;
	}
	
	public java.lang.Double getChargeAmmount() {
		return this.chargeAmmount;
	}
	public void setChargeOrderStatCode(java.lang.String chargeOrderStatCode) {
		this.chargeOrderStatCode = chargeOrderStatCode;
	}
	
	public java.lang.String getChargeOrderStatCode() {
		return this.chargeOrderStatCode;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	public void setChargeFinishTime(java.util.Date chargeFinishTime) {
		this.chargeFinishTime = chargeFinishTime;
	}
	
	public java.util.Date getChargeFinishTime() {
		return this.chargeFinishTime;
	}
	public void setRefundFinishTime(java.util.Date refundFinishTime) {
		this.refundFinishTime = refundFinishTime;
	}
	
	public java.util.Date getRefundFinishTime() {
		return this.refundFinishTime;
	}

	
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getChargeOrderId())
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(this.getChargeOrderId() == null){
			return false;
		}
		if(!(obj instanceof ChargeOrder)){
			return false;
		}
		if(this == obj) {
			return true;
		}
		ChargeOrder other = (ChargeOrder)obj;
		return new EqualsBuilder()
			.append(getChargeOrderId(),other.getChargeOrderId())
			.isEquals();
	}
}

