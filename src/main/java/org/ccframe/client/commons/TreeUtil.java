package org.ccframe.client.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.ccframe.subsys.core.domain.entity.TreeNode;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;

public class TreeUtil {
	
	public final static ModelKeyProvider<TreeNodeTree> treeNodeTreeKeyProvider = new ModelKeyProvider<TreeNodeTree>(){
		@Override
		public String getKey(TreeNodeTree item) {
			return item.getTreeNodeId().toString();
		}
	};

	public final static ValueProvider<TreeNodeTree, String> treeValueProvider = new ValueProvider<TreeNodeTree, String>() {

		@Override
		public String getValue(TreeNodeTree object) {
			return object.getTreeNodeNm();
		}

		@Override
		public void setValue(TreeNodeTree object,String value) {
			object.setTreeNodeNm(value);
		}

		@Override
		public String getPath() {
			return TreeNode.TREE_NODE_NM;
		}
	};
	
	/**
	 * 加载主树.
	 * @param store
	 * @param treeNodeTree
	 * @param showRoot
	 */
	public static final void loadTreeStore(TreeStore<TreeNodeTree> store, TreeNodeTree treeNodeTree, boolean showRoot){
		store.clear();
		if(showRoot){
			store.add(treeNodeTree);
			loadTreeStoreSubNodes(store, treeNodeTree, treeNodeTree.getSubNodeTree());
		}else{
			List<TreeNodeTree> subNodeTreeList = treeNodeTree.getSubNodeTree();
			if(subNodeTreeList != null && !subNodeTreeList.isEmpty()){
				for(TreeNodeTree subNodeTree: subNodeTreeList){
					store.add(subNodeTree);
					loadTreeStoreSubNodes(store, subNodeTree, subNodeTree.getSubNodeTree());
				}
			}
		}
	}
	
	/**
	 * 加载子树.
	 * @param store
	 * @param parent
	 * @param teeNodeTreeList
	 */
	public static void loadTreeStoreSubNodes(TreeStore<TreeNodeTree> store,TreeNodeTree parent, List<TreeNodeTree> teeNodeTreeList){
		if(teeNodeTreeList == null || teeNodeTreeList.isEmpty()){
			return;
		}
		for(TreeNodeTree treeNodeTree: teeNodeTreeList){
			store.add(parent, treeNodeTree);
			loadTreeStoreSubNodes(store,treeNodeTree, treeNodeTree.getSubNodeTree());
		}
	}
	
	/**
	 * 获得指定id在树的路径.
	 * 如果为游离节点则返回空List,需要时可以去join
	 * @param treeNodeId
	 * @param root
	 * @return
	 */
	public static List<String> getNamePath(Integer treeNodeId, TreeNodeTree root){
		List<String> resultList = new ArrayList<String>();
		getNamePath(resultList, new Stack<TreeNodeTree>(), treeNodeId, root);
		return resultList;
	}
	
	private static void getNamePath(List<String> resultList, Stack<TreeNodeTree> workStack, Integer treeNodeId, TreeNodeTree workNode){
		workStack.push(workNode);
		if(workNode.getTreeNodeId() == treeNodeId){
			//找到path，将节点快照复制到resultList
			for(TreeNodeTree treeNodeTree: workStack){
				resultList.add(treeNodeTree.getTreeNodeNm());
			}
		}
		if(!resultList.isEmpty()){
			return;
		}
		for(TreeNodeTree treeNodeTree: workNode.getSubNodeTree()){
			getNamePath(resultList, workStack, treeNodeId, treeNodeTree);
		}
		workStack.pop();
	}
}
