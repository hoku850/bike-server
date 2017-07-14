package org.ccframe.client.base;

import com.google.gwt.uibinder.client.UiField;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

public abstract class BasePagingListView<T> extends BaseListView<T>{

	@UiField
	public PagingToolBar pagingToolBar;

	@Override
	protected void bindOther(){
		pagingToolBar.bind(loader);
	}

}
