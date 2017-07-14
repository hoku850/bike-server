package org.ccframe.client.module.bike.view;

import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.CcFormPanelHelper;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.subsys.bike.domain.entity.BikeType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;

@Singleton
public class UserToRepairWindowView extends BaseWindowView<Integer, BikeType> {

	interface UserToRepairUiBinderUiBinder extends UiBinder<Component, UserToRepairWindowView> {}
	
	private static UserToRepairUiBinderUiBinder uiBinder = GWT.create(UserToRepairUiBinderUiBinder.class);
	
	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;
	
	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		return widget;
	}

	@Override
	protected void onLoadData(Integer bikeTypeId) {
		window.setHeadingText("单车类型" + (bikeTypeId == null ? "增加" : "修改"));
		
		CcFormPanelHelper.clearInvalid(vBoxLayoutContainer);
		if(bikeTypeId == null){
			FormPanelHelper.reset(vBoxLayoutContainer);
		}else{
			FormPanelHelper.reset(vBoxLayoutContainer);
		}
		vBoxLayoutContainer.forceLayout();
	}

}

