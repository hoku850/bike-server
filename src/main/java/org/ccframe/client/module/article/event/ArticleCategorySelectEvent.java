package org.ccframe.client.module.article.event;

import org.ccframe.client.base.BaseHandler;
import org.ccframe.client.base.BaseObjectEvent;
import org.ccframe.client.commons.TreeNodeTree;

@SuppressWarnings("rawtypes")
public class ArticleCategorySelectEvent extends BaseObjectEvent<TreeNodeTree> {

	public static final Type TYPE = new Type();

	public ArticleCategorySelectEvent(TreeNodeTree object) {
		super(object);
	}

    @Override
	public Type<BaseHandler> getAssociatedType() {
		return TYPE;
	}

}
