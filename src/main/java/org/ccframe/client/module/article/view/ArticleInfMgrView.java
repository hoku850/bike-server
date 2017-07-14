package org.ccframe.client.module.article.view;

import org.ccframe.client.commons.ICcModule;
import org.ccframe.client.module.core.event.BodyContentEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.Component;

/**
 * 
 * 文章管理器.
 * 
 * @author JIM
 *
 */
@Singleton
public class ArticleInfMgrView implements ICcModule{

	private Widget widget;
	
	@UiField
	public ArticleInfListView articleInfListView;
	
	@UiField
	public ArticleInfCategoryView articleInfCategoryView;
	
	interface ArticleInfMgrUiBinder extends UiBinder<Component, ArticleInfMgrView> {}
	private static ArticleInfMgrUiBinder uiBinder = GWT.create(ArticleInfMgrUiBinder.class);

	@Override
	public Widget asWidget() {
		if(widget == null){
			widget = uiBinder.createAndBindUi(this);
		}
		return widget;
	}

	@Override
	public void onModuleReload(BodyContentEvent event) {
		articleInfListView.onModuleReload(event);
		articleInfCategoryView.onModuleReload(event);
	}

}
