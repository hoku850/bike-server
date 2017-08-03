package org.ccframe.client.module.bike.view;

import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.CcFormPanelHelper;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.components.CcCurrencyField;
import org.ccframe.client.components.CcIntegerField;
import org.ccframe.client.components.CcTextField;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
import org.ccframe.subsys.bike.dto.CyclingOrderRowDto;
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
public class CyclingOrderFinishWindowView extends BaseWindowView<Integer, CyclingOrderRowDto> implements Editor<CyclingOrderRowDto>{

	interface CyclingOrderFinishUiBinder extends UiBinder<Component, CyclingOrderFinishWindowView> {}
	interface CyclingOrderDriver extends SimpleBeanEditorDriver<CyclingOrderRowDto, CyclingOrderFinishWindowView> {}
	
	private static CyclingOrderFinishUiBinder uiBinder = GWT.create(CyclingOrderFinishUiBinder.class);
	private CyclingOrderDriver driver = GWT.create(CyclingOrderDriver.class);
	
	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;
	
	@UiField
	CcIntegerField cyclingOrderId;
	
	@UiField
	CcTextField orgNm;
	
	@UiField
	CcTextField loginId;
	
	@UiField
	CcTextField hardwareCodeStr;
	
	@UiField
	CcTextField bikePlateNumber;
	
	@UiField
	CcTextField bikeTypeNm;
	
	@UiField
	CcTextField cyclingOrderStatCode;
	
	@UiField
	CcTextField startTimeStr;
	
	@UiField
	CcCurrencyField orderAmmount;
	
	@Editor.Ignore
	@UiField
	TextButton saveButton;
	
	@UiHandler("saveButton")
	public void handleSaveClick(SelectEvent e){
		if(FormPanelHelper.isValid(vBoxLayoutContainer, false)){
			
			final CyclingOrderRowDto cyclingOrderRowDto = driver.flush();
			
			final TextButton button = ((TextButton)(e.getSource()));
			button.disable();

			ClientManager.getCyclingOrderClient().finish(cyclingOrderRowDto, new RestCallback<Integer>(){
				@Override
				public void onSuccess(Method method, Integer response) {
					Info.display("操作完成", " 编号为"+response+"的骑行订单强制结束成功");
					CyclingOrderFinishWindowView.this.retCallBack.onClose(cyclingOrderRowDto);
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
		driver.edit(new CyclingOrderRowDto());
		return widget;
	}

	@Override
	protected void onLoadData(Integer cyclingOrderId) {
		CcFormPanelHelper.clearInvalid(vBoxLayoutContainer);
		// 重置下拉框
		ClientManager.getCyclingOrderClient().finishGetById(cyclingOrderId, new RestCallback<CyclingOrderRowDto>(){
			@Override
			public void onSuccess(Method method, CyclingOrderRowDto response) {
				driver.edit(response);
				if(!(response.getCyclingOrderStatCode().equals(CyclingOrderStatCodeEnum.ON_THE_WAY.toCode()))){
					orderAmmount.disable();
					saveButton.disable();
				}else{
					orderAmmount.enable();
					saveButton.enable();
				}
				switch (CyclingOrderStatCodeEnum.fromCode(response.getCyclingOrderStatCode())){
				case ON_THE_WAY:
					cyclingOrderStatCode.setValue("骑行中");
					break;
				case CYCLING_FINISH:
					cyclingOrderStatCode.setValue("骑行完成");
					break;
				case PAY_FINISH:
					cyclingOrderStatCode.setValue("支付完成");
					break;
				case TO_BE_REPAIRED:
					cyclingOrderStatCode.setValue("已报修");
					break;
				case TEMPORARY_LOCKING:
					cyclingOrderStatCode.setValue("临时锁定");
					break;
				default:
					cyclingOrderStatCode.setValue("出bug啦");
					break;	
				}
			}
		});
		
		vBoxLayoutContainer.forceLayout();
	}

}

