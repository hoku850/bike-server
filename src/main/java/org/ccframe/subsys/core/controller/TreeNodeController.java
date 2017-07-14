package org.ccframe.subsys.core.controller;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.TreeNodeTree;
import org.ccframe.client.commons.TreeRootEnum;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.domain.entity.Role;
import org.ccframe.subsys.core.domain.entity.RoleMenuResRel;
import org.ccframe.subsys.core.domain.entity.TreeNode;
import org.ccframe.subsys.core.service.RoleMenuResRelService;
import org.ccframe.subsys.core.service.RoleService;
import org.ccframe.subsys.core.service.TreeNodeSearchService;
import org.ccframe.subsys.core.service.TreeNodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.TREE_NODE_BASE)
public class TreeNodeController{

	/**
	 * 根据树根获得树.
	 * @param treeRoot
	 * @return
	 */
	@RequestMapping(value = ControllerMapping.TREE_NODE_TREE)
	public TreeNodeTree getTree(Integer treeRoot){
		return SpringContextHelper.getBean(TreeNodeSearchService.class).getTree(treeRoot, null);
	}

	/**
	 * 根据树根获得子树.
	 * @param treeRoot
	 * @return
	 */
	@RequestMapping(value = ControllerMapping.TREE_NODE_SUB_TREE)
	public List<TreeNodeTree> getSubTree(Integer treeRoot){
		return SpringContextHelper.getBean(TreeNodeSearchService.class).getSubNodeTree(treeRoot, null);
	}

	/**
	 * 增改树节点.
	 * @param treeRoot
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void addModify(@RequestBody TreeNodeTree treeNodeTree){
		TreeNode treeNode = new TreeNode();
		BeanUtils.copyProperties(treeNodeTree, treeNode);
		SpringContextHelper.getBean(TreeNodeService.class).save(treeNode);
	}

	/**
	 * 删除树节点.
	 * @param treeRoot
	 * @return
	 */
	@RequestMapping(value = Global.ID_BINDER_PATH, method = RequestMethod.DELETE)
	public void delete(@PathVariable(Global.ID_BINDER_ID) Integer treeNodeId, String deleteCheckBeans){
		SpringContextHelper.getBean(TreeNodeService.class).delete(treeNodeId, deleteCheckBeans);
	}

	/**
	 * 获得当前节点和根之间的路径文本.
	 * @param treeNodeId
	 * @param deleteCheckBeans
	 * @return
	 */
	@RequestMapping(value = ControllerMapping.GET_NAME_PATH)
	public List<String> getNamePath(Integer treeNodeId, Integer rootId){
		return SpringContextHelper.getBean(TreeNodeService.class).getNamePath(treeNodeId, rootId);
	}

	/**
	 * 根据机构获得机构树模板.
	 * @param treeRoot
	 * @return
	 */
	@RequestMapping(value = ControllerMapping.TREE_NODE_ORG_MENU_TREE)
	public TreeNodeTree getOrgMenuTree(Integer orgId){
		List<Role> roleList = SpringContextHelper.getBean(RoleService.class).findByKey(Role.ORG_ID, orgId);
		if(roleList.isEmpty()){
			return null;
		}
		List<RoleMenuResRel> roleMenuResRelList = SpringContextHelper.getBean(RoleMenuResRelService.class).findByKey(RoleMenuResRel.ROLE_ID, roleList.get(0).getRoleId());
		List<Integer> menuResIdList = new ArrayList<Integer>();
		for(RoleMenuResRel roleMenuResRel: roleMenuResRelList){
			menuResIdList.add(roleMenuResRel.getMenuResId());
		}
		return SpringContextHelper.getBean(TreeNodeSearchService.class).getTree(TreeRootEnum.MENU_TREE_ROOT.getTreeNodeId(), menuResIdList);
	}

}
