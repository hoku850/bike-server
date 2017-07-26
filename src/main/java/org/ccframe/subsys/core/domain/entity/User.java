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

import org.ccframe.client.Global;
import org.ccframe.client.commons.UtilDateTimeClient;
import org.ccframe.commons.cache.AutoCacheConfig;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * @author JIM
 * 登录ID、邮箱和登录名之间的关系：
 * 用户注册时包括邮箱注册、手机注册和用户名注册3种方式。
 * 邮箱或手机注册时，LOGIN_ID为手机或邮箱号码，需要进行验证。如果验收不成功，可以通过找回密码继续进行验证找回。
 * 当用户使用用户名注册时，允许用户完善资料验证邮箱或手机。
 * 完善邮箱或手机时，需要通过验证。
 * 
 * 用户登录的逻辑，首先验证用户名是否符合，再验证手机或邮箱是否符合，只要有符合则登录。
 */
@Entity
@Table(name = "SYS_USER")
//elasticsearch
@Document(indexName = Global.ES_DEFAULT_INDEX, type = "user")
@Setting(settingPath = Global.ES_DEFAULT_ANALYSER)
@AutoCacheConfig
public class User implements Serializable{

	private static final long serialVersionUID = 6971226453296699368L;

	public static final String USER_ID = "userId";
	public static final String LOGIN_ID = "loginId";
	public static final String USER_NM = "userNm";
	public static final String USER_PSW = "userPsw";
	public static final String USER_MOBILE = "userMobile";
	public static final String USER_EMAIL = "userEmail";
	public static final String CREATE_DATE = "createDate";
	public static final String USER_STAT_CODE = "userStatCode";
	public static final String IF_ADMIN = "ifAdmin";
	public static final String CREATE_DATE_STR = "createDateStr";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
	@Column(name = "USER_ID", nullable = false, length = 10)
	//elasticsearch
	@org.springframework.data.annotation.Id
	private Integer userId;
	
    @Column(name = "LOGIN_ID", nullable = false, length = 38)
	private String loginId;

    @Column(name = "USER_NM", nullable = false, length = 32)
    @Field(type = FieldType.String, store = false, analyzer="ngram_analyzer")//使用ngram进行单字分词
	private String userNm;

    @Column(name = "USER_PSW", nullable = false, length = 128)
	private String userPsw;

    @Column(name = "USER_MOBILE", nullable = true, length = 17)
	private String userMobile;

    @Column(name = "USER_EMAIL", nullable = true, length = 70)
	private String userEmail;

    @Column(name = "IF_ADMIN", nullable = false, length = 2)
	private String ifAdmin;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE", nullable = false, length = 0)
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern = Global.ISO_8601_DATE_FORMAT)
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = Global.ISO_8601_DATE_FORMAT)
	private Date createDate;

    @Column(name = "USER_STAT_CODE", nullable = false, length = 2)
	private String userStatCode;
	
	public String getIfAdmin() {
		return ifAdmin;
	}

	public void setIfAdmin(String ifAdmin) {
		this.ifAdmin = ifAdmin;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getUserNm() {
		return userNm;
	}

	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}

	public String getUserPsw() {
		return userPsw;
	}

	public void setUserPsw(String userPsw) {
		this.userPsw = userPsw;
	}

	@JsonIgnore
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserStatCode() {
		return userStatCode;
	}

	public void setUserStatCode(String userStatCode) {
		this.userStatCode = userStatCode;
	}

	public String getCreateDateStr(){
		return this.getCreateDate() == null ? null : UtilDateTimeClient.convertDateTimeToString(this.getCreateDate());
	}

	public void setCreateDateStr(String timeStr){
		if(timeStr != null){
			this.setCreateDate(UtilDateTimeClient.convertStringToDateTime(timeStr));
		}
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Override
    public boolean equals(Object obj) {
        if(!(obj instanceof User)){
            return false;
        }
        if(this == obj){
            return true;
        }
        User other = (User)obj;
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
