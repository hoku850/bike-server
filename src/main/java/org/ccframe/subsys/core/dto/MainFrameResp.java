package org.ccframe.subsys.core.dto;

import java.util.List;

import org.ccframe.client.commons.AdminUser;
import org.ccframe.client.commons.TreeNodeTree;
import org.ccframe.subsys.core.domain.entity.MenuRes;

public class MainFrameResp{
	
	/**
	 * 管理员用户
	 */
	private AdminUser adminUser;
	
	/**
	 * 管理员菜单树
	 */
	private TreeNodeTree treeNodeTree;
	
	/**
	 * 快速访问菜单
	 */
	private List<MenuRes> fastMenuRes;
	
	public List<MenuRes> getFastMenuRes() {
		return fastMenuRes;
	}
	
	public void setFastMenuRes(List<MenuRes> fastMenuRes) {
		this.fastMenuRes = fastMenuRes;
	}
	
	public TreeNodeTree getTreeNodeTree() {
		return treeNodeTree;
	}
	public void setTreeNodeTree(TreeNodeTree treeNodeTree) {
		this.treeNodeTree = treeNodeTree;
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

}
