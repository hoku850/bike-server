package org.ccframe.client.module.bike.view;


import org.ccframe.client.Global;
import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.CcFormPanelHelper;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.components.CcCurrencyField;
import org.ccframe.client.components.CcLabelValueCombobox;
import org.ccframe.client.components.CcTextField;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.client.module.core.view.MainFrame;
import org.ccframe.subsys.bike.domain.entity.BikeType;
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
public class BikeTypeWindowView extends BaseWindowView<Integer, BikeType> implements Editor<BikeType>{

	interface BikeTypeUiBinder extends UiBinder<Component, BikeTypeWindowView> {}
	interface BikeTypeDriver extends SimpleBeanEditorDriver<BikeType, BikeTypeWindowView> { }
	
	private static BikeTypeUiBinder uiBinder = GWT.create(BikeTypeUiBinder.class);
	private BikeTypeDriver driver = GWT.create(BikeTypeDriver.class);
	
	private Integer bikeTypeId;
	
	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;
	
	@UiField
	CcTextField bikeTypeNm;
	
    @UiField
    CcLabelValueCombobox orgId;
    
	@UiField
	CcCurrencyField halfhourAmmount;
	
	@UiHandler("saveButton")
	public void handleSaveClick(SelectEvent e){
		if(FormPanelHelper.isValid(vBoxLayoutContainer, false)){
			
			final BikeType bikeType = driver.flush();
			bikeType.setBikeTypeId(bikeTypeId);
			
			final TextButton button = ((TextButton)(e.getSource()));
			button.disable();

			ClientManager.getBikeTypeClient().saveOrUpdate(bikeType, new RestCallback<Void>(){
				@Override
				public void onSuccess(Method method, Void response) {
					Info.display("操作完成", "单车类型" + (bikeTypeId == null ? "新增" : "修改") + "成功");
					BikeTypeWindowView.this.retCallBack.onClose(bikeType); //保存并回传结果数据
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
		driver.edit(new BikeType());
		
		//保留小数位后两位
		//NumberFormat format = new NumberFormat(null, null, false);
		//halfhourAmmount.setFormat(format);

			
		return widget;
	}

	@Override
	protected void onLoadData(Integer bikeTypeId) {
		this.bikeTypeId = bikeTypeId;
		window.setHeadingText("单车类型" + (bikeTypeId == null ? "增加" : "修改"));
		
		CcFormPanelHelper.clearInvalid(vBoxLayoutContainer);
		// 重置下拉框
		orgId.reset();
		if(bikeTypeId == null){
			FormPanelHelper.reset(vBoxLayoutContainer);
			// 运营商登陆
			if (Global.PLATFORM_ORG_ID != MainFrame.getAdminUser().getOrgId()) {
				orgId.setValue(MainFrame.getAdminUser().getOrgId());
				orgId.setEnabled(false);
			}
		}else{
			ClientManager.getBikeTypeClient().getById(bikeTypeId, new RestCallback<BikeType>(){
				@Override
				public void onSuccess(Method method, BikeType response) {
					driver.edit(response);
					// 运营商登陆
					if (Global.PLATFORM_ORG_ID != MainFrame.getAdminUser().getOrgId()) {
						orgId.setEnabled(false);
					}
				}
			});
		}
		vBoxLayoutContainer.forceLayout();
	}

}

