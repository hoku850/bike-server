package org.ccframe.client.module.bike.view;

import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.CcFormPanelHelper;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.components.CcEnumCombobox;
import org.ccframe.client.components.CcLabelValueCombobox;
import org.ccframe.client.components.CcTextField;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
public class SmartLockWindowView extends BaseWindowView<Integer, SmartLock> implements Editor<SmartLock>{

	interface SmartLockUiBinderUiBinder extends UiBinder<Component, SmartLockWindowView> {}
	interface SmartLockDriver extends SimpleBeanEditorDriver<SmartLock, SmartLockWindowView> {}
	
	private static SmartLockUiBinderUiBinder uiBinder = GWT.create(SmartLockUiBinderUiBinder.class);
	private SmartLockDriver driver = GWT.create(SmartLockDriver.class);
	
	private Integer smartLockId;
	
	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;
	
	@UiField
	CcTextField lockerHardwareCode;
	
	@UiField
	CcTextField imeiCode;
	
	@UiField
	CcTextField macAddress;
	
	@UiField
	CcTextField bikePlateNumber;
	
    @UiField
    CcLabelValueCombobox orgId;
    
    @UiField
    CcLabelValueCombobox bikeTypeId;
    
    @UiField
    CcEnumCombobox smartLockStatCode;
    
	
	@UiHandler("saveButton")
	public void handleSaveClick(SelectEvent e){
		if(FormPanelHelper.isValid(vBoxLayoutContainer, false)){
			
			final SmartLock smartLock = driver.flush();
			smartLock.setSmartLockId(smartLockId);
			if(imeiCode.getValue()==null){
				imeiCode.setValue("");
			}
			if(macAddress.getValue()==null){
				macAddress.setValue("");
			}
			if(bikePlateNumber.getValue()==null){
				bikePlateNumber.setValue("");
			}
			final TextButton button = ((TextButton)(e.getSource()));
			button.disable();

			ClientManager.getSmartLockClient().saveOrUpdate(smartLock, new RestCallback<Void>(){
				@Override
				public void onSuccess(Method method, Void response) {
					Info.display("操作完成", "智能锁" + (smartLockId == null ? "新增" : "修改") + "成功");
					SmartLockWindowView.this.retCallBack.onClose(smartLock); //保存并回传结果数据
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
		driver.edit(new SmartLock());
		orgId.reset();
		bikeTypeId.reset();
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
		this.smartLockId = smartLockId;
		window.setHeadingText("智能锁" + (smartLockId == null ? "增加" : "修改"));
		if(smartLockId != null){
			orgId.setReadOnly(false);
			bikeTypeId.setReadOnly(false);
		}else{
			orgId.setReadOnly(true);
			bikeTypeId.setReadOnly(true);
		}
		CcFormPanelHelper.clearInvalid(vBoxLayoutContainer);
		// 重置下拉框
		orgId.reset();
		bikeTypeId.reset();
		orgId.addValueChangeHandler(new ValueChangeHandler<Integer>(){

			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				bikeTypeId.setExtraParam(event.getValue().toString());
				bikeTypeId.reset();
			}
			
		});
		if(smartLockId == null){
			FormPanelHelper.reset(vBoxLayoutContainer);
			
		}else{
			ClientManager.getSmartLockClient().getById(smartLockId, new RestCallback<SmartLock>(){
				@Override
				public void onSuccess(Method method, SmartLock response) {
					driver.edit(response);
					orgId.setValue(response.getOrgId());
					bikeTypeId.setValue(response.getBikeTypeId());
					smartLockStatCode.setValue(response.getSmartLockStatCode());
				}
			});
		}
		
		vBoxLayoutContainer.forceLayout();
	}

}

