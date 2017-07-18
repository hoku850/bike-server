package org.ccframe.client.module.bike.view;

import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.CcFormPanelHelper;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.components.CcTextField;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.subsys.core.domain.entity.User;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;

@Singleton
public class AgentTipWindowView extends BaseWindowView<Integer, User> implements Editor<User>{

	interface AgentTipUiBinder extends UiBinder<Component, AgentTipWindowView> {}
	interface AgentTipDriver extends SimpleBeanEditorDriver<User, AgentTipWindowView> {}
	
	private static AgentTipUiBinder uiBinder = GWT.create(AgentTipUiBinder.class);
	private AgentTipDriver driver = GWT.create(AgentTipDriver.class);
	
	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;
	
	@UiField
	CcTextField loginId;
	
	@UiField
	CcTextField userPsw;
	
	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		driver.initialize(this);
		driver.edit(new User());
		return widget;
	}

	@Override
	protected void onLoadData(Integer id) {
		window.setHeadingText("运营商默认管理员账号");

		CcFormPanelHelper.clearInvalid(vBoxLayoutContainer);
		// 重置下拉框
		if(id == null){
			FormPanelHelper.reset(vBoxLayoutContainer);
		}else{
			ClientManager.getAdminUserClient().getById(id, new RestCallback<User>(){
				@Override
				public void onSuccess(Method method, User response) {
					response.setUserPsw("test");
					driver.edit(response);
				}
			});
		}
		
		vBoxLayoutContainer.forceLayout();
	}

}