package org.ccframe.client.commons;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.subsys.core.domain.entity.TreeNode;

public class TreeNodeTree extends TreeNode{
	
	private static final long serialVersionUID = 8849790533771490251L;

	private List<TreeNodeTree> subNodeTree = new ArrayList<TreeNodeTree>();
	
	public List<TreeNodeTree> getSubNodeTree() {
		return subNodeTree;
	}

	public void setSubNodeTree(List<TreeNodeTree> subNodeTree) {
		this.subNodeTree = subNodeTree;
	}
	
}
