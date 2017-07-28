package org.ccframe.client.module.bike.view;

import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.CcFormPanelHelper;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.ViewUtil;
import org.ccframe.client.components.CcLabelValueCombobox;
import org.ccframe.client.components.CcTextField;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.dto.SmartLockGrant;
import org.ccframe.subsys.bike.dto.SmartLockGrantDto;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.info.Info;

@Singleton
public class SmartLockGrantWindowView extends BaseWindowView<Integer, SmartLockGrant>{

	interface SmartLockGrantUiBinder extends UiBinder<Component, SmartLockGrantWindowView> {}
	
	private static SmartLockGrantUiBinder uiBinder = GWT.create(SmartLockGrantUiBinder.class);
	
	private ToggleGroup toggle = new ToggleGroup();
	
	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;
	
	@UiField
	Radio LockerHardwareCodeScope;
	
	@UiField
	Radio bikePlateNumberPrefix;
	
	@UiField
	CcTextField startLockerHardwareCode;
	
	@UiField
	CcTextField endLockerHardwareCode;
	
	@UiField
	CcTextField bikePlateNumberPrefixText;
	
    @UiField
    CcLabelValueCombobox orgId;
	
	@UiField
    CcLabelValueCombobox bikeTypeId;
	
	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		toggle.add(LockerHardwareCodeScope);
		toggle.add(bikePlateNumberPrefix);
		LockerHardwareCodeScope.setValue(true);
		bikePlateNumberPrefixText.disable();
		startLockerHardwareCode.enable();
		endLockerHardwareCode.enable();
		toggle.addValueChangeHandler(new ValueChangeHandler<HasValue<Boolean>>(){
			@Override
			public void onValueChange(ValueChangeEvent<HasValue<Boolean>> event) {
				if(LockerHardwareCodeScope.getValue()==true){
					bikePlateNumberPrefixText.disable();
					startLockerHardwareCode.enable();
					endLockerHardwareCode.enable();
				}else{
					bikePlateNumberPrefixText.enable();
					startLockerHardwareCode.disable();
					endLockerHardwareCode.disable();
				}
			}
		});
		orgId.setAfterAsyncReset(new Runnable(){

			@Override
			public void run() {
				bikeTypeId.setExtraParam(orgId.getValue().toString());
				bikeTypeId.reset();
			}
		});
		orgId.addValueChangeHandler(new ValueChangeHandler<Integer>(){

			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				bikeTypeId.setExtraParam(event.getValue().toString());
				bikeTypeId.reset();
			}
			
		});
		return widget;
	}

	@Override
	protected void onLoadData(Integer smartLockId) {
		CcFormPanelHelper.clearInvalid(vBoxLayoutContainer);
		// 重置下拉框
		orgId.reset();
		FormPanelHelper.reset(vBoxLayoutContainer);
		vBoxLayoutContainer.forceLayout();
	}

	
	@UiHandler("grantButton")
	public void handleGrantClick(SelectEvent e){
		if(FormPanelHelper.isValid(vBoxLayoutContainer, false)){
			final SmartLockGrant smartLockGrant = new SmartLockGrant();
			if(LockerHardwareCodeScope.getValue()==true){
				smartLockGrant.setStartLockerHardwareCode(startLockerHardwareCode.getValue());
				smartLockGrant.setEndLockerHardwareCode(endLockerHardwareCode.getValue());
			}else{
				smartLockGrant.setBikePlateNumberPrefixText(bikePlateNumberPrefixText.getValue());
			}
			smartLockGrant.setOrgId(orgId.getValue());
			smartLockGrant.setBikeTypeId(bikeTypeId.getValue());
			final TextButton button = ((TextButton)(e.getSource()));
//			button.disable();
			
			ClientManager.getSmartLockGrantClient().grantSearch(smartLockGrant, new RestCallback<Long>(){
				
				@Override
				public void onSuccess(Method method, Long response) {
					System.out.println(response);
					ViewUtil.confirm("提示信息", "符合条件的车锁共计"+"<span style='color:red'>"+response+"</span>"+"把"+","+"你确定发放吗？该操作将不可撤销！！", new Runnable(){
						@Override
						public void run() {
							ClientManager.getSmartLockGrantClient().grant(smartLockGrant, new RestCallback<SmartLockGrantDto>(){
								@Override
								public void onSuccess(Method method, SmartLockGrantDto response) {
									Info.display("操作完成", "成功发放单车车锁 "+response.getTotalLock()+" 把至运营商 "+response.getOrgNm());
									SmartLockGrantWindowView.this.retCallBack.onClose(null); //保存并回传结果数据
									button.enable();
									window.hide();
								}
								@Override
								protected void afterFailure(){ //如果采用按钮的disable逻辑，一定要在此方法enable按钮
									button.enable();
								}
								
							});
						}
					});
				}
				@Override
				protected void afterFailure(){ //如果采用按钮的disable逻辑，一定要在此方法enable按钮
					button.enable();
				}
				
			});
		}
	}
}

