package org.ccframe.client.module.bike.view;

import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.CcFormPanelHelper;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.components.CcLabelValueCombobox;
import org.ccframe.client.components.CcTextField;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.subsys.bike.domain.entity.AgentApp;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.info.Info;

@Singleton
public class AgentAppWindowView extends BaseWindowView<Integer, AgentApp> implements Editor<AgentApp>{

	interface AgentAppUiBinder extends UiBinder<Component, AgentAppWindowView> {}
	interface AgentAppDriver extends SimpleBeanEditorDriver<AgentApp, AgentAppWindowView> {}
	
	private static AgentAppUiBinder uiBinder = GWT.create(AgentAppUiBinder.class);
	private AgentAppDriver driver = GWT.create(AgentAppDriver.class);
	
	private Integer agentAppId;
	
	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;
	
	@UiField
	CcTextField appNm;
	
	@UiField
	CcTextField isoUrl;
	
	@UiField
	CcTextField androidUrl;
	
	@UiField
	CcLabelValueCombobox orgId;
	
	@UiHandler("saveButton")
	public void handleSaveClick(SelectEvent e){
		if(FormPanelHelper.isValid(vBoxLayoutContainer, false)){
			
			final AgentApp agentApp = driver.flush();
			agentApp.setAgentAppId(agentAppId);
			
			final TextButton button = ((TextButton)(e.getSource()));
			button.disable();

			ClientManager.getAgentAppClient().saveOrUpdate(agentApp, new RestCallback<Void>(){
				@Override
				public void onSuccess(Method method, Void response) {
					Info.display("操作完成", "运营商APP" + (agentAppId == null ? "新增" : "修改") + "成功");
					AgentAppWindowView.this.retCallBack.onClose(agentApp); //保存并回传结果数据
					button.enable();
					window.hide();
				}
				@Override
				protected void afterFailure(){ //如果采用按钮的disable逻辑，一定要在此方法enable按钮
					button.enable();
				}
			});
		}
	}
	
	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		driver.initialize(this);
		driver.edit(new AgentApp());
		return widget;
	}

	@Override
	protected void onLoadData(Integer agentAppId) {
		this.agentAppId = agentAppId;
		window.setHeadingText("运营商APP" + (agentAppId == null ? "增加" : "修改"));
		orgId.reset();
		CcFormPanelHelper.clearInvalid(vBoxLayoutContainer);
		// 重置下拉框
		if(agentAppId == null){
			FormPanelHelper.reset(vBoxLayoutContainer);
		}else{
			ClientManager.getAgentAppClient().getById(agentAppId, new RestCallback<AgentApp>(){
				@Override
				public void onSuccess(Method method, AgentApp response) {
					driver.edit(response);
				}
			});
		}
		
		vBoxLayoutContainer.forceLayout();
	}

}

