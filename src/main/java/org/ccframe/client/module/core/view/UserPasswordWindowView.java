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
public class UserPasswordWindowView extends BaseWindowView<Integer, Void>{

	interface UserPasswordUiBinder extends UiBinder<Component, UserPasswordWindowView> {}
	
	private static UserPasswordUiBinder uiBinder = GWT.create(UserPasswordUiBinder.class);

	private Integer userId;

	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;

	@UiField
	CcPasswordField userPsw;
	
	@UiField
	CcPasswordField userPswCheck;

	@UiHandler("saveButton")
	public void handleSaveClick(SelectEvent e){
		if(FormPanelHelper.isValid(vBoxLayoutContainer, false)){
			final TextButton button = ((TextButton)(e.getSource()));
			button.disable();
			ClientManager.getMainFrameClient().updatePassword(userId, Sha512Util.encode(userPsw.getValue()), new RestCallback<Void>(){
				@Override
				public void onSuccess(Method method, Void response) {
					Info.display("操作完成", "用户密码更改成功");
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
		userPsw.addValidator(new RegExValidator(Global.USER_PASSWORD_PATTERN, "请输入8位以上密码，必须包含字符和数字"));
		userPswCheck.addValidator(new AbstractValidator<String>(){
			@Override
			public List<EditorError> validate(Editor<String> field, String value) {
			    if (value == null) return null;
			    if (!userPswCheck.getValue().equals(userPsw.getValue())) {
			      return createError(new DefaultEditorError(field, "两次输入的密码不一致", value));
			    }
			    return null;
			}
		});
		return widget;
	}

	@Override
	protected void onLoadData(Integer userId) {
		this.userId = userId;
		this.window.setHeadingText("修改" + (userId == null ? "当前" : "") + "用户密码");
		userPsw.clear();
		userPswCheck.clear();
	}


}

