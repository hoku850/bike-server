package org.ccframe.client.module.core.view;

import org.ccframe.client.base.ImportBaseWindowView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.Component;

@Singleton
public class UserImportWindowView extends ImportBaseWindowView{

	interface UserUploadUiBinder extends UiBinder<Component, UserImportWindowView> {}
	
	private static UserUploadUiBinder uiBinder = GWT.create(UserUploadUiBinder.class);

	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		return widget;
	}

	@Override
	protected String getTemplateFileName() {
		return "userImportExample.xls";
	}

	protected String getUploadDataProviderBeanName(){
		return "userService";
	}
	
}

