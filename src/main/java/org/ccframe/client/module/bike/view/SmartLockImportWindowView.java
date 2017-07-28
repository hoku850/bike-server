package org.ccframe.client.module.bike.view;

import org.ccframe.client.base.ImportBaseWindowView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.Component;

@Singleton
public class SmartLockImportWindowView extends ImportBaseWindowView{

	interface SmartLockUploadUiBinder extends UiBinder<Component, SmartLockImportWindowView> {}
	
	private static SmartLockUploadUiBinder uiBinder = GWT.create(SmartLockUploadUiBinder.class);

//	@UiField
//	TextButton deleteFile;
//	
//	@UiField
//	TextButton previewFile;
	
	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		return widget;
	}

	@Override
	protected String getTemplateFileName() {
		return "smartLockImportExample.xls";
	}

	protected String getUploadDataProviderBeanName(){
		return "smartLockService";
	}
	
}

