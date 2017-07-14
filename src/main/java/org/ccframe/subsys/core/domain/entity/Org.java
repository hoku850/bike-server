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
@Table(name = "SYS_ORG")
@AutoCacheConfig
//elasticsearch
@Document(indexName = "org_index", type = "org")
@Setting(settingPath = "elasticsearch-analyser.json")
public class Org implements Serializable{

	public static final String ORG_ID = "orgId";
	public static final String ROLE_ID = "roleId";
	public static final String ORG_NM = "orgNm";
	public static final String MANAGER = "manager";
	public static final String MANAGER_TEL = "managerTel";
	public static final String IF_DELETE = "ifDelete";
	public static final String REMARK = "remark";

	public static final String ORG_ID_FIELD = "ORG_ID";
	public static final String ROLE_ID_FIELD = "ROLE_ID";
	public static final String ORG_NM_FIELD = "ORG_NM";
	public static final String MANAGER_FIELD = "MANAGER";
	public static final String MANAGER_TEL_FIELD = "MANAGER_TEL";
	public static final String IF_DELETE_FIELD = "IF_DELETE";
	public static final String REMARK_FIELD = "REMARK";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4566163983385212163L;

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
    @Column(name = ORG_ID_FIELD, nullable = false, length = 10)
	// elasticsearch
	@org.springframework.data.annotation.Id
	private Integer orgId;
	
    @Column(name = ROLE_ID_FIELD, nullable = false, length = 10)
	private Integer roleId;

    @Column(name = ORG_NM_FIELD, nullable = false, length = 32)
	private String orgNm;

    @Column(name = MANAGER_FIELD, nullable = true, length = 24)
	private String manager;

    @Column(name = MANAGER_TEL_FIELD, nullable = true, length = 32)
	private String managerTel;

    @Column(name = IF_DELETE_FIELD, nullable = false, length = 2)
	private String ifDelete;

    @Column(name = REMARK_FIELD, nullable = true, length = 255)
	private String remark;


	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getOrgNm() {
		return orgNm;
	}

	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getManagerTel() {
		return managerTel;
	}

	public void setManagerTel(String managerTel) {
		this.managerTel = managerTel;
	}

	public String getIfDelete() {
		return ifDelete;
	}

	public void setIfDelete(String ifDelete) {
		this.ifDelete = ifDelete;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Org)){
			return false;
		}
		if(this == obj){
			return true;
		}
		Org other = (Org)obj;
		return new EqualsBuilder()
		.append(getOrgId(),other.getOrgId())
		.isEquals();
	}

	@Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getOrgId())
                .toHashCode();
    }

}
