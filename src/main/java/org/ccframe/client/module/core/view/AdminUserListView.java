package org.ccframe.client.module.core.view;

import java.util.List;

import org.ccframe.client.ViewResEnum;
import org.ccframe.client.base.BaseCrudListView;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.client.commons.ColumnConfigEx;
import org.ccframe.client.commons.EventBusUtil;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.RestyGwtPagingLoader;
import org.ccframe.client.commons.RestyGwtPagingLoader.CallBack;
import org.ccframe.client.commons.ViewUtil;
import org.ccframe.client.commons.WindowEventCallback;
import org.ccframe.client.module.core.event.LoadWindowEvent;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.code.UserStatCodeEnum;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.dto.UserListReq;
import org.ccframe.subsys.core.dto.UserRowDto;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.info.Info;

@Singleton
public class AdminUserListView extends BaseCrudListView<UserRowDto>{


	interface UserListUiBinder extends UiBinder<Component, AdminUserListView> {}
	private static UserListUiBinder uiBinder = GWT.create(UserListUiBinder.class);
	
	@UiField
	public TextField userNmSearch;
	
	@UiField
	public TextField loginIdSearch;
	
	@UiHandler("searchButton")
	public void searchButtonClick(SelectEvent e){
		loader.load();
	}
	
	@UiHandler("editUserPasswordButton")
	public void editUserPasswordButtonClick(SelectEvent e){
		UserRowDto selectedRow = grid.getSelectionModel().getSelectedItem();
		if(selectedRow == null){
			ViewUtil.error("系统信息", "请选择一条记录进行编辑");
			return;
		}
		
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, Void, EventHandler>(
				ViewResEnum.USER_PASSWORD_WINDOW,
				selectedRow.getUserId(), 
				new WindowEventCallback<Void>(){
			@Override
			public void onClose(Void voidData) {
			}
		}));
	}
	
	@UiHandler("importUserButton")
	public void importUserButtonClick(SelectEvent e){
		EventBusUtil.fireEvent(new LoadWindowEvent<Void, Void, EventHandler>(
			ViewResEnum.USER_IMPORT_WINDOW,
			null,
			new WindowEventCallback<Void>(){
				@Override
				public void onClose(Void returnData) {
					// TODO Auto-generated method stub
				}
			}
		));
	}
	
	@Override
	protected ModelKeyProvider<UserRowDto> getModelKeyProvider() {
		return new ModelKeyProvider<UserRowDto>(){
			@Override
			public String getKey(UserRowDto item) {
				return item.getUserId().toString();
			}
		};
	}

	interface UserProperties extends PropertyAccess<UserRowDto> {
		
		ValueProvider<UserRowDto, Integer> userId();
		ValueProvider<UserRowDto, String> loginId();
		ValueProvider<UserRowDto, String> userNm();
		ValueProvider<UserRowDto, String> userMobile();
		ValueProvider<UserRowDto, String> userEmail();
		
		ValueProvider<UserRowDto, String> userStatCode();
		ValueProvider<UserRowDto, String> createDateStr();
	}
	
	@Override
	protected void initColumnConfig(List<ColumnConfig<UserRowDto, ?>> configList) {
		UserProperties props = GWT.create(UserProperties.class);
		ColumnConfigEx<UserRowDto, String> idColumn = new ColumnConfigEx<UserRowDto, String>(props.loginId(), 180, "用户ID", HasHorizontalAlignment.ALIGN_CENTER, false);
		idColumn.setCell(new AbstractCell<String>(){
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
				UserRowDto user = store.get(context.getIndex());
				sb.appendEscaped(user.getLoginId());
				if(BoolCodeEnum.fromCode(user.getIfAdmin()).boolValue()){
					sb.appendHtmlConstant("<span style='color:red'>(管理员)</span>");
				}
				if(UserStatCodeEnum.fromCode(user.getUserStatCode()) == UserStatCodeEnum.FREEZE){
					sb.appendHtmlConstant("<span style='color:red'>(冻结)</span>");
				}
			}
		});
		configList.add(idColumn);
		configList.add(new ColumnConfigEx<UserRowDto, String>(props.userMobile(), 110, "手机", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<UserRowDto, String>(props.userEmail(), 220, "邮箱", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<UserRowDto, String>(props.userNm(), 140, "姓名", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<UserRowDto, String>(props.createDateStr(), 150, "注册时间", HasHorizontalAlignment.ALIGN_CENTER, true));
	}

	@Override
	public void handleAddClick(SelectEvent e) {
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, User, EventHandler>(ViewResEnum.ADMIN_USER_WINDOW, null, new WindowEventCallback<User>(){
			@Override
			public void onClose(User returnData) {
				loader.load(); //保存完毕后刷新
			}
		}));
	}


	@Override
	protected void onEditClick(SelectEvent e) {
		UserRowDto selectedRow = grid.getSelectionModel().getSelectedItem();
		
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, User, EventHandler>(
				ViewResEnum.ADMIN_USER_WINDOW,
				selectedRow.getUserId(), 
				new WindowEventCallback<User>(){
			@Override
			public void onClose(User returnData) {
				loader.load(); //保存完毕后刷新，可能有过滤条件，因此采用刷新方式
			}
		}));
	}

	@Override
	protected void doDeleteClick(SelectEvent e) {
		ClientManager.getAdminUserClient().delete(grid.getSelectionModel().getSelectedItem().getUserId(), new RestCallback<Void>(){
			@Override
			public void onSuccess(Method method, Void response) {
				Info.display("操作完成", "用户删除成功");
				loader.load(); //保存完毕后刷新，可能有过滤条件，因此采用刷新方式
			}
		});
	}


	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		return widget;
	}

	@Override
	protected CallBack<UserRowDto> getRestReq() {
		
		return new CallBack<UserRowDto>(){
			@Override
			public void call(int offset, int limit,final RestyGwtPagingLoader<UserRowDto> loader) {
				UserListReq userListReq = new UserListReq();
				userListReq.setUserNm(userNmSearch.getValue());
				userListReq.setLoginId(loginIdSearch.getValue());
				ClientManager.getAdminUserClient().findUserList(userListReq, offset, limit, new RestCallback<ClientPage<UserRowDto>>(){
					@Override
					public void onSuccess(Method method, ClientPage<UserRowDto> response) {
						loader.onLoad(response.getList(), response.getTotalLength(), response.getOffset());
					}
				});
			}
		};
	}

}
