 package org.ccframe.client.commons;

/**
 * 树节点定义，如果子业务有新的业务树，请覆盖它。
 * 
 * @author JIM
 *
 */
public enum TreeRootEnum {
	MENU_TREE_ROOT(2),
	ARTICLE_CATEGORY_TREE_ROOT(3);

	private int treeNodeId;
	private TreeRootEnum(int treeNodeId){
		this.treeNodeId = treeNodeId;
	}
	public int getTreeNodeId(){
		return treeNodeId;
	}

}
