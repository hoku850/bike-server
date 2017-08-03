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
@Table(name = "SYS_TREE_NODE")
//elasticsearch
@Document(indexName = Global.ES_DEFAULT_INDEX, type = "treeNode")
@Setting(settingPath = Global.ES_DEFAULT_ANALYSER)
@AutoCacheConfig
public class TreeNode implements Serializable{

	private static final long serialVersionUID = 6971226453296699368L;

	public static final String TREE_NODE_ID = "treeNodeId";
	public static final String UPPER_TREE_NODE_ID = "upperTreeNodeId";
	public static final String TREE_NODE_TYPE_CODE = "treeNodeTypeCode";
	public static final String SYS_OBJECT_ID = "sysObjectId";
	public static final String ICON = "icon";
	public static final String TREE_NODE_NM = "treeNodeNm";
	public static final String TREE_NODE_POSITION = "treeNodePosition";
	public static final String IF_SYS_RESERVE = "ifSysReserve";

	public static final String TREE_NODE_ID_FIELD = "TREE_NODE_ID";
	public static final String UPPER_TREE_NODE_ID_FIELD = "UPPER_TREE_NODE_ID";
	public static final String TREE_NODE_TYPE_CODE_FIELD = "TREE_NODE_TYPE_CODE";
	public static final String SYS_OBJECT_ID_FIELD = "SYS_OBJECT_ID";
	public static final String ICON_FIELD = "ICON";
	public static final String TREE_NODE_NM_FIELD = "TREE_NODE_NM";
	public static final String TREE_NODE_POSITION_FIELD = "TREE_NODE_POSITION";
	public static final String IF_SYS_RESERVE_FIELD = "IF_SYS_RESERVE";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
	@Column(name = TREE_NODE_ID_FIELD, nullable = false, length = 10)
	//elasticsearch
	@org.springframework.data.annotation.Id
	private Integer treeNodeId;
	
    @Column(name = UPPER_TREE_NODE_ID_FIELD, nullable = false, length = 10)
	private Integer upperTreeNodeId;

    @Column(name = TREE_NODE_TYPE_CODE_FIELD, nullable = false, length = 2)
	private String treeNodeTypeCode;

    @Column(name = SYS_OBJECT_ID_FIELD, nullable = true, length = 10)
	private Integer sysObjectId;

    @Column(name = ICON_FIELD, nullable = true, length = 64)
	private String icon;

    @Column(name = TREE_NODE_NM_FIELD, nullable = false, length = 32)
	private String treeNodeNm;

    @Column(name = TREE_NODE_POSITION_FIELD, nullable = false, length = 10)
	private Integer treeNodePosition;
    
    @Column(name=IF_SYS_RESERVE_FIELD, nullable=false, length=2)
	private String ifSysReserve;

	public Integer getTreeNodeId() {
		return treeNodeId;
	}

	public void setTreeNodeId(Integer treeNodeId) {
		this.treeNodeId = treeNodeId;
	}

	public Integer getUpperTreeNodeId() {
		return upperTreeNodeId;
	}

	public void setUpperTreeNodeId(Integer upperTreeNodeId) {
		this.upperTreeNodeId = upperTreeNodeId;
	}

	public String getTreeNodeTypeCode() {
		return treeNodeTypeCode;
	}

	public void setTreeNodeTypeCode(String treeNodeTypeCode) {
		this.treeNodeTypeCode = treeNodeTypeCode;
	}

	public Integer getSysObjectId() {
		return sysObjectId;
	}

	public void setSysObjectId(Integer sysObjectId) {
		this.sysObjectId = sysObjectId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTreeNodeNm() {
		return treeNodeNm;
	}

	public void setTreeNodeNm(String treeNodeNm) {
		this.treeNodeNm = treeNodeNm;
	}

	public Integer getTreeNodePosition() {
		return treeNodePosition;
	}

	public void setTreeNodePosition(Integer treeNodePosition) {
		this.treeNodePosition = treeNodePosition;
	}
	
	public String getIfSysReserve() {
		return ifSysReserve;
	}
	public void setIfSysReserve(String ifSysReserve) {
		this.ifSysReserve = ifSysReserve;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof TreeNode)){
			return false;
		}
		if(this == obj){
			return true;
		}
		TreeNode other = (TreeNode)obj;
		return new EqualsBuilder()
		.append(getTreeNodeId(),other.getTreeNodeId())
		.isEquals();
	}

	@Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getTreeNodeId())
                .toHashCode();
    }

	@Override
	public String toString() {
		return "TreeNode [treeNodeId=" + treeNodeId + ", upperTreeNodeId="
				+ upperTreeNodeId + ", treeNodeTypeCode=" + treeNodeTypeCode
				+ ", sysObjectId=" + sysObjectId + ", icon=" + icon
				+ ", treeNodeNm=" + treeNodeNm + ", treeNodePosition="
				+ treeNodePosition + "]";
	}
}


