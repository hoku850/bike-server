package org.ccframe.client.module.bike.view;

import org.ccframe.client.Global;
import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.CcFormPanelHelper;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.ViewUtil;
import org.ccframe.client.components.CcMobileField;
import org.ccframe.client.components.CcTextField;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.dto.OrgDto;
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
public class OrgWindowView extends BaseWindowView<Integer, OrgDto> implements Editor<OrgDto>{

	interface OrgUiBinder extends UiBinder<Component, OrgWindowView> {}
	interface OrgDriver extends SimpleBeanEditorDriver<OrgDto, OrgWindowView> {}
	
	private static OrgUiBinder uiBinder = GWT.create(OrgUiBinder.class);
	private OrgDriver driver = GWT.create(OrgDriver.class);
	
	private Integer agentId;
	
	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;
	
	@UiField
	CcTextField orgNm;
	
	@UiField
	CcTextField manager;
	
	@UiField
	CcMobileField managerTel;
	
	@UiHandler("saveButton")
	public void handleSaveClick(SelectEvent e){
		if(FormPanelHelper.isValid(vBoxLayoutContainer, false)){
			
			final OrgDto orgDto = driver.flush();
			orgDto.setOrgId(agentId);
			
			final TextButton button = ((TextButton)(e.getSource()));
			button.disable();

			ClientManager.getOrgClient().saveOrUpdate(orgDto, new RestCallback<User>(){
				@Override
				public void onSuccess(Method method, User response) {
					Info.display("操作完成", "运营商" + (agentId == null ? "新增" : "修改") + "成功");
					OrgWindowView.this.retCallBack.onClose(orgDto); //保存并回传结果数据
					button.enable();
					window.hide();
					// 新增成功后提示该运营商的账户和密码
					if (agentId == null && response != null) {
						String message = "成功添加运营商：" + orgDto.getOrgNm() + "<br>运营商默认管理员帐号：" + response.getLoginId() + "<br>运营商默认管理员密码：" + Global.AGRNT_DEFAULT_PASSWORD;
						ViewUtil.messageBox("成功添加运营商", message);
					}
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
		driver.edit(new OrgDto());
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
			ClientManager.getOrgClient().getById(agentId, new RestCallback<OrgDto>(){
				@Override
				public void onSuccess(Method method, OrgDto response) {
					driver.edit(response);
				}
			});
		}
		
		vBoxLayoutContainer.forceLayout();
	}

}

