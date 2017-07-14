package org.ccframe.subsys.core.service;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.TreeNodeTree;
import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.subsys.core.domain.entity.TreeNode;
import org.ccframe.subsys.core.search.TreeNodeSearchRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TreeNodeSearchService extends BaseSearchService<TreeNode, Integer, TreeNodeSearchRepository> {

	/**
	 * 返回全树.
	 * filterTreeNodeIdList用于在拼装节点时过滤，如果非null，则必须在此列表的才能继续查找子树
	 * @param treeNodeId
	 * @return
	 */
	public TreeNodeTree getTree(int treeNodeId, List<Integer> filterObjectIdList){
		TreeNodeTree treeNodeTree = new TreeNodeTree();
		TreeNode treeNode = getById(treeNodeId);
		BeanUtils.copyProperties(treeNode, treeNodeTree);
		treeNodeTree.setSubNodeTree(getSubNodeTree(treeNodeId, filterObjectIdList));
		return treeNodeTree;
	}
	
	/**
	 * 返回子树列表
	 * @param treeNodeId
	 * @return
	 */
	public List<TreeNodeTree> getSubNodeTree(int treeNodeId,List<Integer> filterObjectIdList){
		return getSubNodeTree(null, treeNodeId, filterObjectIdList);
	}
	
	private List<TreeNodeTree> getSubNodeTree(List<Integer> nodeIdList, int treeNodeId,List<Integer> filterObjectIdList){
		List<TreeNode> subNodes = findByKey(TreeNode.UPPER_TREE_NODE_ID, treeNodeId, new Order(Direction.ASC, TreeNode.TREE_NODE_POSITION));
		List<TreeNodeTree> result = new ArrayList<TreeNodeTree>();
		for(TreeNode treeNode: subNodes){
			if(filterObjectIdList != null && ! filterObjectIdList.contains(treeNode.getSysObjectId())){ //如果为过滤模式，那么不要修剪树枝节点，判断依据是没有对象ID
				continue;
			}
			TreeNodeTree treeNodeTree = new TreeNodeTree();
			BeanUtils.copyProperties(treeNode, treeNodeTree);
			if(nodeIdList != null){
				nodeIdList.add(treeNodeTree.getTreeNodeId());
			}
			treeNodeTree.setSubNodeTree(getSubNodeTree(nodeIdList, treeNode.getTreeNodeId(), filterObjectIdList));
			result.add(treeNodeTree);
		}
		return result;
	}
	
	/**
	 * 子树ID列表，用于IN查询.
	 * @param treeNodeId
	 * @return
	 */
	public List<Integer> getTreeNodeIdList(Integer treeNodeId,List<Integer> filterObjectIdList){
		List<Integer> nodeIdList = new ArrayList<Integer>();
		if(treeNodeId != null){
			nodeIdList.add(treeNodeId);
			getSubNodeTree(nodeIdList, treeNodeId, filterObjectIdList);
		}
		return nodeIdList;
	}

}
