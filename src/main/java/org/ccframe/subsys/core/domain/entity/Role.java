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
@Table(name = "SYS_ROLE")
@Document(indexName = "default_index", type = "role")
@Setting(settingPath = "elasticsearch-analyser.json")
@AutoCacheConfig
public class Role implements Serializable{

	private static final long serialVersionUID = 6971226453296699368L;

	public static final String ROLE_ID = "roleId";
	public static final String ROLE_NM = "roleNm";
	public static final String IF_TEMPLATE = "ifTemplate";
	public static final String ORG_ID = "orgId";

	public static final String ROLE_ID_FIELD = "ROLE_ID";
	public static final String ROLE_NM_FIELD = "ROLE_NM";
	public static final String IF_TEMPLATE_FIELD = "IF_TEMPLATE";
	public static final String ORG_ID_FIELD = "ORG_ID";
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
	@Column(name = ROLE_ID_FIELD, nullable = false, length = 10)
	//elasticsearch
	@org.springframework.data.annotation.Id
	private Integer roleId;

    @Column(name = ROLE_NM_FIELD, nullable = false, length = 32)
	private String roleNm;

    @Column(name = IF_TEMPLATE_FIELD, nullable = false, length = 2)
	private String ifTemplate;

    @Column(name = ORG_ID_FIELD, nullable = false, length = 10)
	private Integer orgId;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleNm() {
		return roleNm;
	}

	public void setRoleNm(String roleNm) {
		this.roleNm = roleNm;
	}

	public String getIfTemplate() {
		return ifTemplate;
	}

	public void setIfTemplate(String ifTemplate) {
		this.ifTemplate = ifTemplate;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	@Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Role)){
            return false;
        }
        if(this == obj){
            return true;
        }
        Role other = (Role)obj;
        return new EqualsBuilder()
                .append(getRoleId(),other.getRoleId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getRoleId())
                .toHashCode();
    }

}
