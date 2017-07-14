package org.ccframe.client.module.core.view;

import java.util.List;

import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.EventBusUtil;
import org.ccframe.client.commons.ICcModule;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.ViewUtil;
import org.ccframe.client.components.CcTextField;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.client.module.core.event.AdminRoleSelectEvent;
import org.ccframe.client.module.core.event.BodyContentEvent;
import org.ccframe.client.module.core.event.RoleSelectEvent;
import org.ccframe.subsys.core.domain.entity.Role;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.info.Info;

@Singleton
public class AdminRoleListView implements ICcModule{

	public Widget widget; 

	interface AdminRoleListUiBinder extends UiBinder<Component, AdminRoleListView> {}
	private static AdminRoleListUiBinder uiBinder = GWT.create(AdminRoleListUiBinder.class);
	
	@UiField
	TextButton addButton;

	@UiField
	TextButton editButton;

	@UiField
	TextButton deleteButton;

	@UiField(provided = true)
	ListView<Role, String> roleList;
	
	ListStore<Role> listStore;

	private Window simpleAddModifyWin;
	
	private TextButton simpleAddModifyButton;

	private CcTextField roleNm;

	Integer selectedId;
	
	@UiHandler({"addButton","editButton"})
	public void handleAddClick(SelectEvent e){
		boolean isAdd = (e.getSource() == addButton);
		simpleAddModifyWin.setHeadingText((isAdd ? "新增": "修改") + "角色"); //例如 新增文章分类

		if(isAdd){
			selectedId = null;
		}else{
			selectedId = roleList.getSelectionModel().getSelectedItem().getRoleId();
		}

		simpleAddModifyWin.show();
		simpleAddModifyWin.center();
	}

	private void reloadList(){
		ClientManager.getAdminRoleClient().findRoleList(new RestCallback<List<Role>>(){
			@Override
			public void onSuccess(Method method, List<Role> response) {
				listStore.clear();
				listStore.addAll(response);
				roleList.getSelectionModel().select(0, false); //默认选择第一个
			}
		});
	}

	@UiHandler("deleteButton")
	public void handleDeleteClick(SelectEvent e){
		if(listStore.size() <= 1){
			ViewUtil.error("系统信息", "至少保留一个角色");
			return;
		}
		final Role selectItem = roleList.getSelectionModel().getSelectedItem();
		
		ViewUtil.confirm("系统信息", "您确定要删除角色 " + selectItem.getRoleNm() + " 吗？删除后将不可恢复", new Runnable(){
			@Override
			public void run() {
				//二次确认
				ClientManager.getAdminRoleClient().getRefUserCount(selectItem.getRoleId(), new RestCallback<Integer>(){
					@Override
					public void onSuccess(Method method, Integer response) {
						if(response > 0){
							ViewUtil.confirm("系统信息", "角色 " + selectItem.getRoleNm() + " 已被 " + response + "个用户使用，删除将解除这些用户与角色的关系，确定继续？", new Runnable(){
								@Override
								public void run() {
									ClientManager.getAdminRoleClient().delete(selectItem.getRoleId(),new RestCallback<Void>(){
										@Override
										public void onSuccess(Method method, Void response) {
											reloadList();
										}
									});
								}
							});
						}
					}
				});
			}
		});
	}

	@Override
	public Widget asWidget() {
		if(widget == null){
			
			listStore = new ListStore<Role>(new ModelKeyProvider<Role>(){
				@Override
				public String getKey(Role item) {
					return item.getRoleId().toString();
				}
			});
			roleList = new ListView<Role, String>(listStore, new ValueProvider<Role, String>(){
				@Override
				public String getValue(Role role) {
					return role.getRoleNm();
				}
				@Override
				public void setValue(Role role, String value) {
				}
				@Override
				public String getPath() {
					return null;
				}
				
			});
			widget = uiBinder.createAndBindUi(this);
			roleList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			roleList.getSelectionModel().addSelectionHandler(new SelectionHandler<Role>(){
				@Override
				public void onSelection(SelectionEvent<Role> selection) {
					EventBusUtil.fireEvent(new AdminRoleSelectEvent(selection.getSelectedItem()));
				}
			});

			roleNm = new CcTextField();
			roleNm.setWidth(150);
			roleNm.setAllowBlank(false);
			roleNm.setMaxLength(32);

			FieldLabel roleNmRow = new FieldLabel(roleNm, "角色名称");

			simpleAddModifyButton = new TextButton("保存", new SelectHandler(){
				@Override
				public void onSelect(SelectEvent event) {
					if(!roleNm.validate()){
						return;
					}

					final Role role = new Role();
					role.setRoleId(selectedId);
					role.setRoleNm(roleNm.getValue());
					ClientManager.getAdminRoleClient().saveOrUpdate(role, new RestCallback<Void>(){
						@Override
						public void onSuccess(Method method, Void response) {
							Info.display("保存角色", "保存成功！");
							simpleAddModifyWin.hide();
							if(selectedId == null){
								reloadList();
							}else{
								listStore.update(role);
							}
						}
					});
				}
			});

			simpleAddModifyWin = new Window();
			simpleAddModifyWin.setWidth(300);
			simpleAddModifyWin.setModal(true);
			CcVBoxLayoutContainer vBoxLayoutContainer = new CcVBoxLayoutContainer();
			vBoxLayoutContainer.add(roleNmRow, new BoxLayoutData(new Margins(10, 10, 5, 10)));
			simpleAddModifyWin.setWidget(vBoxLayoutContainer);
			simpleAddModifyWin.addButton(simpleAddModifyButton);
			simpleAddModifyWin.addButton(new TextButton("取消", new SelectHandler(){
				@Override
				public void onSelect(SelectEvent event) {
					simpleAddModifyWin.hide();
				}
			}));
			simpleAddModifyWin.setResizable(false);
			simpleAddModifyWin.setButtonAlign(BoxLayoutPack.CENTER);

			roleList.getSelectionModel().addSelectionHandler(new SelectionHandler<Role>(){
				@Override
				public void onSelection(SelectionEvent<Role> event) {
					EventBusUtil.fireEvent(new RoleSelectEvent(event.getSelectedItem()));
				}
			});
		}
		return widget;
	}

	@Override
	public void onModuleReload(BodyContentEvent event) {
		reloadList();
	}

}
