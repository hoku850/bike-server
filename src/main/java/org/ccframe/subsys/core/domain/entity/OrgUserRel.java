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
@Table(name = "SYS_ORG_USER_REL")
@Document(indexName = Global.ES_DEFAULT_INDEX, type = "orgUserRel")
@Setting(settingPath = Global.ES_DEFAULT_ANALYSER)
@AutoCacheConfig
public class OrgUserRel implements Serializable{

	private static final long serialVersionUID = -5711645334453919166L;

	public static final String USER_ORG_REL_ID = "userOrgRelId";
	public static final String USER_ID = "userId";
	public static final String ORG_ID = "orgId";

	public static final String USER_ORG_REL_ID_FIELD = "USER_ORG_REL_ID";
	public static final String USER_ID_FIELD = "USER_ID";
	public static final String ORG_ID_FIELD = "ORG_ID";

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
    @Column(name = USER_ORG_REL_ID_FIELD, nullable = false, length = 10)
	@org.springframework.data.annotation.Id
	private Integer orgUserRelId;
	
    @Column(name = USER_ID_FIELD, nullable = false, length = 10)
	private Integer userId;

    @Column(name = ORG_ID_FIELD, nullable = false, length = 10)
	private Integer orgId;

	public Integer getOrgUserRelId() {
		return orgUserRelId;
	}

	public void setOrgUserRelId(Integer orgUserRelId) {
		this.orgUserRelId = orgUserRelId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof OrgUserRel)){
			return false;
		}
		if(this == obj){
			return true;
		}
		OrgUserRel other = (OrgUserRel)obj;
		return new EqualsBuilder()
		.append(getOrgUserRelId(),other.getOrgUserRelId())
		.isEquals();
	}

	@Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getOrgUserRelId())
                .toHashCode();
    }

}
