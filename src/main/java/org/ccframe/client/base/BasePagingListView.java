package org.ccframe.client.base;

import org.ccframe.client.components.CcPagingToolBar;

import com.google.gwt.uibinder.client.UiField;

public abstract class BasePagingListView<T> extends BaseListView<T>{

	@UiField
	public CcPagingToolBar pagingToolBar;

	@Override
	protected void bindOther(){
		pagingToolBar.bind(loader);
	}

}
