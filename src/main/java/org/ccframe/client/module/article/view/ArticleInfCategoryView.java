package org.ccframe.client.module.article.view;

import org.ccframe.client.base.BaseTreeView;
import org.ccframe.client.commons.EventBusUtil;
import org.ccframe.client.commons.TreeNodeTree;
import org.ccframe.client.commons.TreeRootEnum;
import org.ccframe.client.module.article.event.ArticleCategorySelectEvent;
import org.ccframe.subsys.core.domain.code.TreeNodeTypeCodeEnum;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.Component;

@Singleton
public class ArticleInfCategoryView extends BaseTreeView{

	interface ArticleInfCategoryUiBinder extends UiBinder<Component, ArticleInfCategoryView> {}
	private static ArticleInfCategoryUiBinder uiBinder = GWT.create(ArticleInfCategoryUiBinder.class);

	@Override
	protected Widget bindUi() {
		return uiBinder.createAndBindUi(this);
	}

	@Override
	protected Integer getTreeRootId() {
		return TreeRootEnum.ARTICLE_CATEGORY_TREE_ROOT.getTreeNodeId();
	}

	@Override
	protected TreeNodeTypeCodeEnum getTreeNodeTypeCodeEnum() {
		return TreeNodeTypeCodeEnum.ARTICLE_CATEGORY;
	}

	@Override
	protected SelectionHandler<TreeNodeTree> getTreeSelectionHandler() {
		return new SelectionHandler<TreeNodeTree>(){
			@Override
			public void onSelection(SelectionEvent<TreeNodeTree> selection) {
				TreeNodeTree selectedItem = selection.getSelectedItem();
				//如果是根则清除分类条件以便优化
				EventBusUtil.fireEvent(new ArticleCategorySelectEvent(selectedItem.equals(treeStore.getChild(0)) ? null : selection.getSelectedItem()));
			}
		};
	}

	@Override
	protected String getCheckBeans() {
		return "articleInfService";
	}


}
