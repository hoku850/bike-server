package org.ccframe.subsys.core.domain.entity;

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
@Table(name = "SYS_ROLE_MENU_RES_REL")
@Document(indexName = Global.ES_DEFAULT_INDEX, type = "roleMenuResRel")
@Setting(settingPath = Global.ES_DEFAULT_ANALYSER)
@AutoCacheConfig
public class RoleMenuResRel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4771493549895497560L;

	public static final String ROLE_MENU_RES_REL_ID = "roleMenuResRelId";
	public static final String MENU_RES_ID = "menuResId";
	public static final String ROLE_ID = "roleId";

	public static final String ROLE_MENU_RES_REL_ID_FIELD = "ROLE_MENU_RES_REL_ID";
	public static final String MENU_RES_ID_FIELD = "MENU_RES_ID";
	public static final String ROLE_ID_FIELD = "ROLE_ID";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
	@Column(name = ROLE_MENU_RES_REL_ID_FIELD, nullable = false, length = 10)
	@org.springframework.data.annotation.Id
	private Integer roleMenuResRelId;
	
    @Column(name = MENU_RES_ID_FIELD, nullable = false, length = 10)
	private Integer menuResId;

    @Column(name = ROLE_ID_FIELD, nullable = false, length = 10)
	private Integer roleId;

	public Integer getRoleMenuResRelId() {
		return roleMenuResRelId;
	}

	public void setRoleMenuResRelId(Integer roleMenuResRelId) {
		this.roleMenuResRelId = roleMenuResRelId;
	}

	public Integer getMenuResId() {
		return menuResId;
	}

	public void setMenuResId(Integer menuResId) {
		this.menuResId = menuResId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof RoleMenuResRel)){
			return false;
		}
		if(this == obj){
			return true;
		}
		RoleMenuResRel other = (RoleMenuResRel)obj;
		return new EqualsBuilder()
		.append(getRoleMenuResRelId(),other.getRoleMenuResRelId())
		.isEquals();
	}

	@Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getRoleMenuResRelId())
                .toHashCode();
    }

}
