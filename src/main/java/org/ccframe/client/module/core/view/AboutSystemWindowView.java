package org.ccframe.client.module.core.view;

import org.ccframe.client.base.BaseWindowView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.Component;

@Singleton
public class AboutSystemWindowView extends BaseWindowView<Integer, Void>{

	interface UserPasswordUiBinder extends UiBinder<Component, AboutSystemWindowView> {}
	
	private static UserPasswordUiBinder uiBinder = GWT.create(UserPasswordUiBinder.class);

	private Integer userId;

/*	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;
*/
	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		return widget;
	}

	@Override
	protected void onLoadData(Integer userId) {
		this.userId = userId;
	}


}

