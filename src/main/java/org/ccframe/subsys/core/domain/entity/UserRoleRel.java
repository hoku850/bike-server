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
@Table(name = "SYS_USER_ROLE_REL")
@Document(indexName = Global.ES_DEFAULT_INDEX, type = "userRoleRel")
@Setting(settingPath = Global.ES_DEFAULT_ANALYSER)
@AutoCacheConfig
public class UserRoleRel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String USER_ID = "userId";
	public static final String ROLE_ID = "roleId";

	public static final String USER_ROLE_REL_ID_FIELD = "USER_ROLE_REL_ID";
	public static final String USER_ID_FIELD = "USER_ID";
	public static final String ROLE_ID_FIELD = "ROLE_ID";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
	@Column(name = USER_ROLE_REL_ID_FIELD, nullable = false, length = 10)
	@org.springframework.data.annotation.Id
	private Integer userRoleRelId;

    @Column(name = USER_ID_FIELD, nullable = false, length = 10)
	private Integer userId;
	
    @Column(name = ROLE_ID_FIELD, nullable = false, length = 10)
	private Integer roleId;

	public Integer getUserRoleRelId() {
		return userRoleRelId;
	}

	public void setUserRoleRelId(Integer userRoleRelId) {
		this.userRoleRelId = userRoleRelId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
	@Override
    public boolean equals(Object obj) {
        if(!(obj instanceof UserRoleRel)){
            return false;
        }
        if(this == obj){
            return true;
        }
        UserRoleRel other = (UserRoleRel)obj;
        return new EqualsBuilder()
                .append(getUserRoleRelId(),other.getUserRoleRelId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getUserRoleRelId())
                .toHashCode();
    }


}


