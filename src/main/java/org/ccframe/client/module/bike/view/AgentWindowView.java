package org.ccframe.client.module.bike.view;

import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.CcFormPanelHelper;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.components.CcPhoneField;
import org.ccframe.client.components.CcTextField;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.subsys.bike.domain.entity.Agent;
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
public class AgentWindowView extends BaseWindowView<Integer, Agent> implements Editor<Agent>{

	interface AgentUiBinder extends UiBinder<Component, AgentWindowView> {}
	interface AgentDriver extends SimpleBeanEditorDriver<Agent, AgentWindowView> {}
	
	private static AgentUiBinder uiBinder = GWT.create(AgentUiBinder.class);
	private AgentDriver driver = GWT.create(AgentDriver.class);
	
	private Integer agentId;
	
	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;
	
	@UiField
	CcTextField agentNm;
	
	@UiField
	CcTextField manager;
	
	@UiField
	CcPhoneField managerTel;
	
	@UiHandler("saveButton")
	public void handleSaveClick(SelectEvent e){
		if(FormPanelHelper.isValid(vBoxLayoutContainer, false)){
			
			final Agent Agent = driver.flush();
			Agent.setAgentId(agentId);
			
			final TextButton button = ((TextButton)(e.getSource()));
			button.disable();

			ClientManager.getAgentClient().saveOrUpdate(Agent, new RestCallback<Void>(){
				@Override
				public void onSuccess(Method method, Void response) {
					Info.display("操作完成", "运营商" + (agentId == null ? "新增" : "修改") + "成功");
					AgentWindowView.this.retCallBack.onClose(Agent); //保存并回传结果数据
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
		driver.edit(new Agent());
		return widget;
	}

	@Override
	protected void onLoadData(Integer agentId) {
		this.agentId = agentId;
		window.setHeadingText("运营商" + (agentId == null ? "增加" : "修改"));

		CcFormPanelHelper.clearInvalid(vBoxLayoutContainer);
		// 重置下拉框
		if(agentId == null){
			FormPanelHelper.reset(vBoxLayoutContainer);
		}else{
			ClientManager.getAgentClient().getById(agentId, new RestCallback<Agent>(){
				@Override
				public void onSuccess(Method method, Agent response) {
					driver.edit(response);
				}
			});
		}
		
		vBoxLayoutContainer.forceLayout();
	}

}

