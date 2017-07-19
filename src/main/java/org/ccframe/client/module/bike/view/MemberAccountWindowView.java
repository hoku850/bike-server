package org.ccframe.client.module.bike.view;

import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.CcFormPanelHelper;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.components.CcCurrencyField;
import org.ccframe.client.components.CcEnumRadioField;
import org.ccframe.client.components.CcTextArea;
import org.ccframe.client.components.CcTextField;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.subsys.core.domain.code.AccountTypeCodeEnum;
import org.ccframe.subsys.core.domain.code.ChargeOperateCodeEnum;
import org.ccframe.subsys.core.dto.MemberAccountRowDto;
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
import com.sencha.gxt.widget.core.client.form.DoubleField;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.info.Info;

@Singleton
public class MemberAccountWindowView extends BaseWindowView<Integer, MemberAccountRowDto> implements Editor<MemberAccountRowDto>{

	interface MemberAccountUiBinderUiBinder extends UiBinder<Component, MemberAccountWindowView> {}
	interface MemberAccountDriver extends SimpleBeanEditorDriver<MemberAccountRowDto, MemberAccountWindowView> { }
	
	private static MemberAccountUiBinderUiBinder uiBinder = GWT.create(MemberAccountUiBinderUiBinder.class);
	private MemberAccountDriver driver = GWT.create(MemberAccountDriver.class);
	
	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;
	
	@UiField
	public CcTextField userNm;
	
	@UiField
	public CcTextField orgNm;
	
	@UiField
	public CcTextField accountTypeCode;
	
	@UiField
	public DoubleField accountValue;
	
	@UiField
	public CcEnumRadioField chargeOperateTypeCode;
	
	@UiField
	public CcCurrencyField changeValue;
	
	@UiField
	public CcTextArea reason;
	
	@UiHandler("saveButton")
	public void handleSaveClick(SelectEvent e){
		if(FormPanelHelper.isValid(vBoxLayoutContainer, false)){
			
			final TextButton button = ((TextButton)(e.getSource()));
			button.disable();
			
			final MemberAccountRowDto memberAccountRowDto = driver.flush();
			switch (memberAccountRowDto.getAccountTypeCode()) {
			case "积分":
				memberAccountRowDto.setAccountTypeCode(AccountTypeCodeEnum.INTEGRAL.toCode());
				break;
			case "预存款":
				memberAccountRowDto.setAccountTypeCode(AccountTypeCodeEnum.DEPOSIT.toCode());
				break;
			case "押金":
				memberAccountRowDto.setAccountTypeCode(AccountTypeCodeEnum.INTEGRAL.toCode());
				break;
			default:
				break;
			}

			ClientManager.getMemberAccountClient().saveOrUpdate(memberAccountRowDto, new RestCallback<Void>() {
				
				@Override
				public void onSuccess(Method method, Void response) {
					Info.display("操作完成", "充值成功");
					MemberAccountWindowView.this.retCallBack.onClose(memberAccountRowDto); //保存并回传结果数据
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
		driver.edit(new MemberAccountRowDto());
		return widget;
	}

	@Override
	protected void onLoadData(Integer memberAccountId) {
//		window.setHeadingText("充值/扣费");
		CcFormPanelHelper.clearInvalid(vBoxLayoutContainer);
		if(memberAccountId == null){
			FormPanelHelper.reset(vBoxLayoutContainer);
		}else{
			ClientManager.getMemberAccountClient().getById(memberAccountId, new RestCallback<MemberAccountRowDto>(){
				@Override
				public void onSuccess(Method method, MemberAccountRowDto response) {
					driver.edit(response);
					chargeOperateTypeCode.setValue(ChargeOperateCodeEnum.RECHARGE.toCode());
					switch (AccountTypeCodeEnum.fromCode(response.getAccountTypeCode())) {
					case INTEGRAL:
						accountTypeCode.setValue("积分");
						break;
					case PRE_DEPOSIT:
						accountTypeCode.setValue("预存款");
						break;
					case DEPOSIT:
						accountTypeCode.setValue("押金");
						break;
					}
				}
			});
		}
		vBoxLayoutContainer.forceLayout();
	}

}

