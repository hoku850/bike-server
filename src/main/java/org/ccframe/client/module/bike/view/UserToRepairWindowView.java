package org.ccframe.client.module.bike.view;

import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.CcFormPanelHelper;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.subsys.bike.domain.entity.BikeType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;

@Singleton
public class UserToRepairWindowView extends BaseWindowView<Integer, BikeType> {

	interface UserToRepairUiBinderUiBinder extends UiBinder<Component, UserToRepairWindowView> {}
	
	private static UserToRepairUiBinderUiBinder uiBinder = GWT.create(UserToRepairUiBinderUiBinder.class);
	
	@UiField
	Frame frame;
	
	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;
	
	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		return widget;
	}

	@Override
	protected void onLoadData(Integer id) {
		window.setHeadingText("报修位置显示");
		
		CcFormPanelHelper.clearInvalid(vBoxLayoutContainer);
		if(id == null){
			FormPanelHelper.reset(vBoxLayoutContainer);
		}else{
			frame.setUrl(GWT.getHostPageBaseURL() + "map/umap.jsp?userToRepairRecordId=" + id);
			frame.setPixelSize(445, 350);
		}
		
		vBoxLayoutContainer.forceLayout();
	}

}

