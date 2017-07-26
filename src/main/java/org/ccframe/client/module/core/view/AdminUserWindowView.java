package org.ccframe.client.module.core.view;

import org.ccframe.client.Global;
import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.CcFormPanelHelper;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.Sha512Util;
import org.ccframe.client.components.CcBoolRadioField;
import org.ccframe.client.components.CcEmailField;
import org.ccframe.client.components.CcMobileField;
import org.ccframe.client.components.CcPasswordField;
import org.ccframe.client.components.CcTextField;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.subsys.core.domain.code.UserStatCodeEnum;
import org.ccframe.subsys.core.domain.entity.User;
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
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import com.sencha.gxt.widget.core.client.info.Info;

@Singleton
public class AdminUserWindowView extends BaseWindowView<Integer, User> implements Editor<User>{

	interface AdminUserUiBinder extends UiBinder<Component, AdminUserWindowView> {}
	interface AdminUserDriver extends SimpleBeanEditorDriver<User, AdminUserWindowView> { }
	
	private static AdminUserUiBinder uiBinder = GWT.create(AdminUserUiBinder.class);

	private Integer userId;
	
	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;
	
	@UiField
	CcTextField loginId;

	@UiField
	CcTextField userNm;

	@UiField
	CcMobileField userMobile;

	@UiField
	CcEmailField userEmail;

	@UiField
	CcPasswordField userPsw;
			
	@UiField
	CcBoolRadioField ifAdmin;
	
	@Editor.Ignore
	@UiField
	CcBoolRadioField ifFreeze;
	
	@UiField
	TextField createDateStr;

	@UiHandler("saveButton")
	public void handleSaveClick(SelectEvent e){
		if(FormPanelHelper.isValid(vBoxLayoutContainer, false)){
			final User adminUser = driver.flush();
			adminUser.setUserId(userId);
			if(ifFreeze.getBoolValue()){
				adminUser.setUserStatCode(UserStatCodeEnum.FREEZE.toCode());
			}else{
				adminUser.setUserStatCode(UserStatCodeEnum.NORMAL.toCode());
			}
			final TextButton button = ((TextButton)(e.getSource()));
			button.disable();

			adminUser.setUserPsw(Sha512Util.encode(adminUser.getUserPsw()));
			ClientManager.getAdminUserClient().saveOrUpdate(adminUser, new RestCallback<Void>(){
				@Override
				public void onSuccess(Method method, Void response) {
					Info.display("操作完成", "用户" + (loginId.getValue() == null ? "新增" : "修改") + "成功");
					AdminUserWindowView.this.retCallBack.onClose(adminUser); //保存并回传结果数据
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
	
	private AdminUserDriver driver = GWT.create(AdminUserDriver.class);

	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		driver.initialize(this);
		userPsw.addValidator(new RegExValidator(Global.USER_PASSWORD_PATTERN, "请输入8位以上密码，必须包含字符和数字"));
		driver.edit(new User());
		return widget;
	}

	@Override
	protected void onLoadData(Integer userId) {
		this.userId = userId;
		window.setHeadingText((userId == null ? "新增" : "修改") + "用户");
		createDateStr.getParent().setVisible(userId != null);
		userPsw.getParent().setVisible(userId == null);
		loginId.setReadOnly(userId != null);
		
		CcFormPanelHelper.clearInvalid(vBoxLayoutContainer);
		if(userId == null){
			FormPanelHelper.reset(vBoxLayoutContainer);
		}else{
			ClientManager.getAdminUserClient().getById(userId, new RestCallback<User>(){
				@Override
				public void onSuccess(Method method, User response) {
					driver.edit(response);
					ifFreeze.setBoolValue(UserStatCodeEnum.fromCode(response.getUserStatCode()) == UserStatCodeEnum.FREEZE);
				}
			});
		}
		vBoxLayoutContainer.forceLayout();
	}


}

