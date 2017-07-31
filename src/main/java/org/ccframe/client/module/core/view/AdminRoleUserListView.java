package org.ccframe.client.module.core.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ccframe.client.base.BaseHandler;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.EventBusUtil;
import org.ccframe.client.commons.ICcModule;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.StringUtils;
import org.ccframe.client.commons.ViewUtil;
import org.ccframe.client.module.core.event.BodyContentEvent;
import org.ccframe.client.module.core.event.RoleSelectEvent;
import org.ccframe.subsys.core.domain.entity.Role;
import org.ccframe.subsys.core.domain.entity.User;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
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
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.info.Info;

@Singleton
public class AdminRoleUserListView implements ICcModule{

	public Widget widget; 

	interface AdminRoleUserListUiBinder extends UiBinder<Component, AdminRoleUserListView> {}
	private static AdminRoleUserListUiBinder uiBinder = GWT.create(AdminRoleUserListUiBinder.class);

	private Role role;

	@UiField(provided = true)
	ListView<User, String> userList;
	
	Window addWindow;
	
	TextArea addUsersText; //TODO 使用CcTextArea限制长度
	
	ListStore<User> listStore;

	@UiHandler("addButton")
	public void handleAddClick(SelectEvent e){
		addWindow.show();
		addWindow.center();
		addUsersText.reset();
	}

	@UiHandler("deleteButton")
	public void handleDeleteClick(SelectEvent e){
		int selectedCount = userList.getSelectionModel().getSelectedItems().size();
		if(selectedCount == 0){
			ViewUtil.error("系统信息", "请选择用户解除角色关联");
			return;
		}
		ViewUtil.confirm("提示信息", "您确定解除这" + (selectedCount == 1 ? "" : (" " + Integer.toString(selectedCount) + " ")) + "个用户与  " + role.getRoleNm() + " 角色的关联吗？", new Runnable(){
			@Override
			public void run() {
				List<Integer> userIdList = new ArrayList<Integer>();
				for(User user: userList.getSelectionModel().getSelectedItems()){
					userIdList.add(user.getUserId());
				}				
				
				ClientManager.getAdminRoleClient().batchDeleteRoleUserRel(role.getRoleId(), userIdList, new RestCallback<Void>(){
					@Override
					public void onSuccess(Method method, Void response) {
						Info.display("操作完成", "取消关联角色 " + role.getRoleNm() + " 成功");
						reloadList();
					}
				});
			}
		});
	}

	public void reloadList(){
		ClientManager.getAdminRoleClient().findRoleUserList(role.getRoleId(), new RestCallback<List<User>>(){
			@Override
			public void onSuccess(Method method, List<User> response) {
				listStore.clear();
				listStore.addAll(response);
			}
		});
	}
	
	@Override
	public Widget asWidget() {
		if(widget == null){
			listStore = new ListStore<User>(new ModelKeyProvider<User>(){
				@Override
				public String getKey(User item) {
					return item.getUserId().toString();
				}
			});
			userList = new ListView<User, String>(listStore, new ValueProvider<User, String>(){
				@Override
				public String getValue(User user) {
					return user.getLoginId();
				}
				@Override
				public void setValue(User role, String value) {
				}
				@Override
				public String getPath() {
					return null;
				}
			});
			widget = uiBinder.createAndBindUi(this);
			EventBusUtil.addHandler(RoleSelectEvent.TYPE, new BaseHandler<RoleSelectEvent>(){
				@Override
				public void action(RoleSelectEvent event) {
					role = event.getObject();
					reloadList();
				}
			});

			widget = uiBinder.createAndBindUi(this);
			userList.getSelectionModel().setSelectionMode(SelectionMode.MULTI);

			addWindow = new Window();
			addWindow.setHeadingText("批量关联用户角色");
			addWindow.setPixelSize(280, 250);
			addWindow.setResizable(false);
			addWindow.setModal(true);
			VBoxLayoutContainer vBoxLayoutContainer = new VBoxLayoutContainer();
			vBoxLayoutContainer.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
			HTML html = new HTML(SafeHtmlUtils.fromString("请输入要添加的用户登录ID，每行一个"));
			html.setWordWrap(true);
			vBoxLayoutContainer.add(html);
			addUsersText = new TextArea();
			addUsersText.setAllowBlank(false);
			BoxLayoutData boxLayoutData = new BoxLayoutData();
			boxLayoutData.setMargins(new Margins(5, 0, 0, 0));
			boxLayoutData.setFlex(1.0d);
			vBoxLayoutContainer.add(addUsersText, boxLayoutData);
			addWindow.add(vBoxLayoutContainer, new MarginData(5, 10, 5, 10));
			addWindow.setFocusWidget(addUsersText);

			addWindow.addButton(new TextButton("添加", new SelectHandler(){
				@Override
				public void onSelect(SelectEvent event) {
					if(addUsersText.validate()){
						
						List<String> loginIdList = Arrays.asList(addUsersText.getValue().split("\n"));
						ClientManager.getAdminRoleClient().batchAddRoleUserRel(role.getRoleId(), loginIdList, new RestCallback<List<String>>(){
							@Override
							public void onSuccess(Method method, List<String> response) {
								addWindow.hide();
								if(response.isEmpty()){
									Info.display("操作完成", "用户关联角色 " + role.getRoleNm() + " 成功");
								}else{
									ViewUtil.error("存在操作失败", "以下管理员关联失败<br/>请检查登录ID是否填写错误或管理员不属于该机构：<br/>" + StringUtils.join(response, "<br/>"));
								}
								reloadList();
							}
						});
					}
				}
			}));
			addWindow.addButton(new TextButton("取消", new SelectHandler(){
				@Override
				public void onSelect(SelectEvent event) {
					addWindow.hide();
				}
			}));
			addWindow.setButtonAlign(BoxLayoutPack.CENTER);
		}
		return widget;

	}
	@Override
	public void onModuleReload(BodyContentEvent event) {
		
	}

}
