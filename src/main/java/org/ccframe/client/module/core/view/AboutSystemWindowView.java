package org.ccframe.client.module.core.view;

import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.Sha512Util;
import org.ccframe.client.components.CcPasswordField;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.AbstractValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import com.sencha.gxt.widget.core.client.info.Info;

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

