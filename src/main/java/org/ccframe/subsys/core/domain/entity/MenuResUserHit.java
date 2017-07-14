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
@Table(name = "SYS_MENU_RES_USER_HIT")
@Document(indexName = "default_index", type = "menuResUserHit")
@Setting(settingPath = "elasticsearch-analyser.json")
@AutoCacheConfig
public class MenuResUserHit implements Serializable{

	private static final long serialVersionUID = 6971226453296699368L;

	public static final String MENU_RES_USER_HIT_ID = "menuResUserHitId";
	public static final String MENU_RES_ID = "menuResId";
	public static final String USER_ID = "userId";
	public static final String LAST_HIT_TIME = "lastHitTime";

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
    @Column(name = "MENU_RES_USER_HIT_ID", nullable = false, length = 10)
	@org.springframework.data.annotation.Id
	private Integer menuResUserHitId;

    @Column(name = "MENU_RES_ID", nullable = false, length = 10)
	private Integer menuResId;

    @Column(name = "USER_ID", nullable = false, length = 10)
	private Integer userId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_HIT_TIME", nullable = false, length = 0)
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	private Date lastHitTime;

	public Integer getMenuResId() {
		return menuResId;
	}

	public void setMenuResId(Integer menuResId) {
		this.menuResId = menuResId;
	}

	public Integer getMenuResUserHitId() {
		return menuResUserHitId;
	}

	public void setMenuResUserHitId(Integer menuResUserHitId) {
		this.menuResUserHitId = menuResUserHitId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getLastHitTime() {
		return lastHitTime;
	}

	public void setLastHitTime(Date lastHitTime) {
		this.lastHitTime = lastHitTime;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof MenuResUserHit)){
			return false;
		}
		if(this == obj){
			return true;
		}
		MenuResUserHit other = (MenuResUserHit)obj;
		return new EqualsBuilder()
		.append(getMenuResId(),other.getMenuResId())
		.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
		.append(getMenuResId())
		.toHashCode();
	}
}


