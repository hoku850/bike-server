package org.ccframe.subsys.core.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.ccframe.client.commons.UtilDateTimeClient;
import org.ccframe.commons.cache.AutoCacheConfig;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * @author JIM
 */
@Entity
@Table(name = "SYS_USER_SECURITY_INF")
@AutoCacheConfig
public class UserSecurityInf implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7986457848300378986L;

	public static final String USER_ID = "userId";
	public static final String USER_VALIDATE_EMAIL = "userValidateEmail";
	public static final String EMAIL_VALIDATE_CODE = "emailValidateCode";
	public static final String EMAIL_VALIDATE_EXPIRE_TIME = "emailValidateExpireTime";
	public static final String IF_EMAIL_VALIDATE = "ifEmailValidate";
	public static final String USER_VALIDATE_MOBILE = "userValidateMobile";
	public static final String MOBILE_VALIDATE_CODE = "mobileValidateCode";
	public static final String MOBILE_VALIDATE_EXPIRE_TIME = "mobileValidateExpireTime";
	public static final String IF_MOBILE_VALIDATE = "ifMobileValidate";
	public static final String USER_PAYMENT_PSW = "userPaymentPsw";

	public static final String USER_ID_FIELD = "USER_ID";
	public static final String USER_VALIDATE_EMAIL_FIELD = "USER_VALIDATE_EMAIL";
	public static final String EMAIL_VALIDATE_CODE_FIELD = "EMAIL_VALIDATE_CODE";
	public static final String EMAIL_VALIDATE_EXPIRE_TIME_FIELD = "EMAIL_VALIDATE_EXPIRE_TIME";
	public static final String IF_EMAIL_VALIDATE_FIELD = "IF_EMAIL_VALIDATE";
	public static final String USER_VALIDATE_MOBILE_FIELD = "USER_VALIDATE_MOBILE";
	public static final String MOBILE_VALIDATE_CODE_FIELD = "MOBILE_VALIDATE_CODE";
	public static final String MOBILE_VALIDATE_EXPIRE_TIME_FIELD = "MOBILE_VALIDATE_EXPIRE_TIME";
	public static final String IF_MOBILE_VALIDATE_FIELD = "IF_MOBILE_VALIDATE";
	public static final String USER_PAYMENT_PSW_FIELD = "USER_PAYMENT_PSW";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = USER_ID_FIELD, nullable = false, length = 10)
	private Integer userId;
	
    @Column(name = USER_VALIDATE_EMAIL, nullable = false, length = 64)
	private String userValidateEmail;

    @Column(name = EMAIL_VALIDATE_CODE, nullable = false, length = 32)
	private String emailValidateCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = EMAIL_VALIDATE_EXPIRE_TIME, nullable = false, length = 0)
	private Date emailValidateExpireTime;

    @Column(name = IF_EMAIL_VALIDATE, nullable = false, length = 2)
	private String ifEmailValidate;

    @Column(name = USER_VALIDATE_MOBILE, nullable = false, length = 11)
	private String userValidateMobile;

    @Column(name = MOBILE_VALIDATE_CODE, nullable = false, length = 8)
	private String mobileValidateCode;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = MOBILE_VALIDATE_EXPIRE_TIME, nullable = true, length = 0)
	private Date mobileValidateExpireTime;

    @Column(name = IF_MOBILE_VALIDATE, nullable = false, length = 2)
	private String ifMobileValidate;

    @Column(name = USER_PAYMENT_PSW, nullable = false, length = 30)
	private String userPaymentPsw;


	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserValidateEmail() {
		return userValidateEmail;
	}

	public void setUserValidateEmail(String userValidateEmail) {
		this.userValidateEmail = userValidateEmail;
	}

	public String getEmailValidateCode() {
		return emailValidateCode;
	}

	public void setEmailValidateCode(String emailValidateCode) {
		this.emailValidateCode = emailValidateCode;
	}

	@JsonIgnore
	public Date getEmailValidateExpireTime() {
		return emailValidateExpireTime;
	}

	public void setEmailValidateExpireTime(Date emailValidateExpireTime) {
		this.emailValidateExpireTime = emailValidateExpireTime;
	}

	public String getIfEmailValidate() {
		return ifEmailValidate;
	}

	public void setIfEmailValidate(String ifEmailValidate) {
		this.ifEmailValidate = ifEmailValidate;
	}

	public String getUserValidateMobile() {
		return userValidateMobile;
	}

	public void setUserValidateMobile(String userValidateMobile) {
		this.userValidateMobile = userValidateMobile;
	}

	public String getMobileValidateCode() {
		return mobileValidateCode;
	}

	public void setMobileValidateCode(String mobileValidateCode) {
		this.mobileValidateCode = mobileValidateCode;
	}

	@JsonIgnore
	public Date getMobileValidateExpireTime() {
		return mobileValidateExpireTime;
	}

	public void setMobileValidateExpireTime(Date mobileValidateExpireTime) {
		this.mobileValidateExpireTime = mobileValidateExpireTime;
	}

	public String getIfMobileValidate() {
		return ifMobileValidate;
	}

	public void setIfMobileValidate(String ifMobileValidate) {
		this.ifMobileValidate = ifMobileValidate;
	}

	public String getUserPaymentPsw() {
		return userPaymentPsw;
	}

	public void setUserPaymentPsw(String userPaymentPsw) {
		this.userPaymentPsw = userPaymentPsw;
	}

	//-----------------------

	public String getMobileValidateExpireTimeStr(){
		return this.getMobileValidateExpireTime() == null ? null : UtilDateTimeClient.convertDateTimeToString(this.getMobileValidateExpireTime());
	}

	public void setMobileValidateExpireTimeStr(String mobileValidateExpireTimeStr){
		if(mobileValidateExpireTimeStr != null){
			this.setMobileValidateExpireTime(UtilDateTimeClient.convertStringToDateTime(mobileValidateExpireTimeStr));
		}
	}

	public String getEmailValidateExpireTimeStr(){
		return this.getEmailValidateExpireTime() == null ? null : UtilDateTimeClient.convertDateTimeToString(this.getEmailValidateExpireTime());
	}

	public void setEmailValidateExpireTimeStr(String emailValidateExpireTimeStr){
		if(emailValidateExpireTimeStr != null){
			this.setEmailValidateExpireTime(UtilDateTimeClient.convertStringToDateTime(emailValidateExpireTimeStr));
		}
	}

	
	@Override
    public boolean equals(Object obj) {
        if(!(obj instanceof UserSecurityInf)){
            return false;
        }
        if(this == obj){
            return true;
        }
        UserSecurityInf other = (UserSecurityInf)obj;
        return new EqualsBuilder()
                .append(getUserId(),other.getUserId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getUserId())
                .toHashCode();
    }

}
