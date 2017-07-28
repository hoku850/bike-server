package org.ccframe.client.module.bike.view;

import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.CcFormPanelHelper;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.components.CcTextField;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.subsys.bike.dto.CyclingOrderRowDto;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;

@Singleton
public class CyclingOrderWindowView extends BaseWindowView<Integer, CyclingOrderRowDto> implements Editor<CyclingOrderRowDto>{

	interface CyclingOrderUiBinder extends UiBinder<Component, CyclingOrderWindowView> {}
	interface CyclingOrderDriver extends SimpleBeanEditorDriver<CyclingOrderRowDto, CyclingOrderWindowView> {}
	
	private static CyclingOrderUiBinder uiBinder = GWT.create(CyclingOrderUiBinder.class);
	private CyclingOrderDriver driver = GWT.create(CyclingOrderDriver.class);
	
	@UiField
	Frame frame;
	
	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;
	
	@UiField
	CcTextField userNm;
	
	@UiField
	CcTextField startTimeStr;
	
	@UiField
	CcTextField endTimeStr;
	
	@UiField
	CcTextField continueTimeStr;
	
	@UiField
	CcTextField cyclingDistanceMeterStr;
	
	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		driver.initialize(this);
		driver.edit(new CyclingOrderRowDto());
		return widget;
	}

	@Override
	protected void onLoadData(Integer id) {
		window.setHeadingText("骑行轨迹");

		CcFormPanelHelper.clearInvalid(vBoxLayoutContainer);
		// 重置下拉框
		if(id == null){
			FormPanelHelper.reset(vBoxLayoutContainer);
		}else{
			frame.setUrl(GWT.getHostPageBaseURL() + "map/cmap.jsp?cyclingOrderId=" + id);
			frame.setPixelSize(550, 495);
			ClientManager.getCyclingOrderClient().getCyclingOrderDtoById(id, new RestCallback<CyclingOrderRowDto>(){
				@Override
				public void onSuccess(Method method, CyclingOrderRowDto response) {
					driver.edit(response);
				}
			});
		}
		
		vBoxLayoutContainer.forceLayout();
	}

}