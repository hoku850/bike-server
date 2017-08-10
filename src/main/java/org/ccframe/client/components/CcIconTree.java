package org.ccframe.client.components;

import org.ccframe.client.commons.TreeNodeTree;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiConstructor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class CcIconTree extends Tree<TreeNodeTree, String> {

	@UiConstructor
	public CcIconTree(TreeStore<TreeNodeTree> store, ValueProvider<TreeNodeTree, String> valueProvider) {
		super(store, valueProvider);
	}

	@Override
	protected SafeHtml getCellContent(TreeNodeTree model) { //重新定义有图标的叶节点输出
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		if(isLeaf(model)){
			sb.append(SafeHtmlUtils.fromTrustedString("<li class='cc-tree-icon iconfont icon-" + model.getIcon() + "'></li>"));
		}
		sb.append(SafeHtmlUtils.fromString(model.getTreeNodeNm()));
		return sb.toSafeHtml();
	}
	protected ImageResource calculateIconStyle(TreeNodeTree model) {
		return null;
	}
}
