package org.ccframe.subsys.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ccframe.client.ResGlobal;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.subsys.core.domain.code.TreeNodeTypeCodeEnum;
import org.ccframe.subsys.core.domain.entity.TreeNode;
import org.ccframe.subsys.core.repository.TreeNodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TreeNodeService extends BaseService<TreeNode, Integer, TreeNodeRepository> {

	
	/**
	 * 树文本路径.
	 * @param treeNodeId
	 * @param rootId
	 * @return
	 */
	public List<String> getNamePath(Integer treeNodeId, Integer rootId){
		List<String> result = new ArrayList<String>();
		Integer workNode = treeNodeId;
		for(int i = 0; i < 100; i ++){
			if((workNode == rootId)){
				break;
			}
			TreeNode treeNode = this.getById(workNode);
			result.add(treeNode.getTreeNodeNm());
			workNode = treeNode.getUpperTreeNodeId();
		}
		result.add(getById(rootId).getTreeNodeNm());
		Collections.reverse(result);
		return result;
	}

	/**
	 * 
	 * @param treeNodeId
	 * @param deleteCheckBeans 业务检查Bean，必须实现checkBusiness如果不通过则抛出异常，如果多个则通过逗号分隔
	 */
	@Transactional
	public void delete(Integer treeNodeId, String deleteCheckBeans) {
		TreeNodeService treeNodeService = SpringContextHelper.getBean(TreeNodeService.class);
		if( ! treeNodeService.findByKey(TreeNode.UPPER_TREE_NODE_ID, treeNodeId).isEmpty()){
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"该节点下非空，请先删除下级节点！"});
		}
		for(String deleteCheckBean: deleteCheckBeans.split(",")){ //删除的前置业务检查
			((ITreeNodeDeleteCheck)SpringContextHelper.getBean(deleteCheckBean)).checkBusiness(treeNodeId);
		}
		treeNodeService.deleteById(treeNodeId);
	}

	/**
	 * 根据对象节点查询树节点，用于树勾选.
	 * @param sysObjectIdList
	 * @param treeNodeTypeCodeEnum
	 * @return
	 */
	public List<Integer> findTreeNodeIdListBySysObjectIdList(List<Integer> sysObjectIdList, TreeNodeTypeCodeEnum treeNodeTypeCodeEnum){
		if(sysObjectIdList.isEmpty()){
			return new ArrayList<Integer>();
		}
		return this.getRepository().findTreeNodeIdListBySysObjectIdList(sysObjectIdList, treeNodeTypeCodeEnum.toCode());
	}
}
