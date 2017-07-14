package org.ccframe.subsys.core.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ccframe.commons.cache.AutoCacheConfig;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

@Entity
@Table(name = "SYS_MENU_RES")
@Document(indexName = "default_index", type = "menuRes")
@Setting(settingPath = "elasticsearch-analyser.json")
@AutoCacheConfig
public class MenuRes implements Serializable{

	private static final long serialVersionUID = 6971226453296699368L;

	public static final String MENU_RES_ID = "menuResId";
	public static final String RES_NM = "resNm";
	public static final String RES_URL = "resUrl";
	public static final String ICON = "icon";

	public static final String MENU_RES_ID_FIELD = "MENU_RES_ID";
	public static final String RES_NM_FIELD = "RES_NM";
	public static final String RES_URL_FIELD = "RES_URL";
	public static final String ICON_FIELD = "ICON";

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
    @Column(name = MENU_RES_ID_FIELD, nullable = false, length = 10)
	@org.springframework.data.annotation.Id
	private Integer menuResId;

    @Column(name = RES_NM_FIELD, nullable = false, length = 32)
	private String resNm;

    @Column(name = RES_URL_FIELD, nullable = true, length = 128)
	private String resUrl;

    @Column(name = ICON_FIELD, nullable = true, length = 64)
	private String icon;

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getMenuResId() {
		return menuResId;
	}

	public void setMenuResId(Integer menuResId) {
		this.menuResId = menuResId;
	}

	public String getResNm() {
		return resNm;
	}

	public void setResNm(String resNm) {
		this.resNm = resNm;
	}

	public String getResUrl() {
		return resUrl;
	}

	public void setResUrl(String resUrl) {
		this.resUrl = resUrl;
	}

	public String getIcon() {
		return icon;
	}

	public void setIconNm(String icon) {
		this.icon = icon;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof MenuRes)){
			return false;
		}
		if(this == obj){
			return true;
		}
		MenuRes other = (MenuRes)obj;
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


