package org.ccframe.client.module.core.view;

import org.ccframe.client.commons.ICcModule;
import org.ccframe.client.module.core.event.BodyContentEvent;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.Component;

@Singleton
public class AdminRoleMgrView implements ICcModule{

	private Widget widget;
	
	@UiField
	public AdminRoleListView adminRoleListView;
	
	@UiField
	public AdminRoleMenuView adminRoleMenuView;
	
	@UiField
	public AdminRoleUserListView adminRoleUserListView;
	
	interface AdminRoleMgrUiBinder extends UiBinder<Component, AdminRoleMgrView> {}
	private static AdminRoleMgrUiBinder uiBinder = GWT.create(AdminRoleMgrUiBinder.class);

	@Override
	public Widget asWidget() {
		if(widget == null){
			widget = uiBinder.createAndBindUi(this);
		}
		return widget;
	}

	@Override
	public void onModuleReload(BodyContentEvent event) {
		adminRoleListView.onModuleReload(event);
		adminRoleMenuView.onModuleReload(event);
		adminRoleUserListView.onModuleReload(event);
	}
}
