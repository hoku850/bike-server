package org.ccframe.client.module.bike.view;

import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.ViewResEnum;
import org.ccframe.client.base.BasePagingListView;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.client.commons.ColumnConfigEx;
import org.ccframe.client.commons.EventBusUtil;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.RestyGwtPagingLoader;
import org.ccframe.client.commons.RestyGwtPagingLoader.CallBack;
import org.ccframe.client.commons.ViewUtil;
import org.ccframe.client.commons.WindowEventCallback;
import org.ccframe.client.components.CcLabelValueCombobox;
import org.ccframe.client.module.bike.event.MemberAccountSelectEvent;
import org.ccframe.client.module.core.event.BodyContentEvent;
import org.ccframe.client.module.core.event.LoadWindowEvent;
import org.ccframe.client.module.core.view.MainFrame;
import org.ccframe.subsys.core.domain.code.AccountTypeCodeEnum;
import org.ccframe.subsys.core.dto.MemberAccountListReq;
import org.ccframe.subsys.core.dto.MemberAccountRowDto;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

@Singleton
public class MemberAccountListView extends BasePagingListView<MemberAccountRowDto> {

	interface MemberAccountListUiBinder extends UiBinder<Component, MemberAccountListView> {}
	private static MemberAccountListUiBinder uiBinder = GWT.create(MemberAccountListUiBinder.class);
	
	private AccountTypeCodeEnum acCodeEnum = AccountTypeCodeEnum.INTEGRAL;
	private boolean isSelect = true;
	
	@UiField
	public TextField searchField;
	
	@UiField
	public TabPanel statusTabPanel;
	
    @UiField
    public CcLabelValueCombobox orgCombobox;
    
    @UiField
    public TextButton chargeButton;
    
    @UiHandler("searchButton")
	public void searchButtonClick(SelectEvent e){
		loader.load();
	}
	
	@UiHandler("chargeButton")
	public void chargeButtononClick(SelectEvent e){
		if(grid.getSelectionModel().getSelectedItems().size() != 1){
			ViewUtil.error("系统信息", "请选择一个账户进行充值/扣费");
			return;
		}
		MemberAccountRowDto memberAccountRowDto = grid.getSelectionModel().getSelectedItem();
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, MemberAccountRowDto, EventHandler>(ViewResEnum.MEMBER_ACCOUNT_WINDOW, memberAccountRowDto.getMemberAccountId(), new WindowEventCallback<MemberAccountRowDto>(){
			@Override
			public void onClose(MemberAccountRowDto returnData) {
				loader.load(); //保存完毕后刷新
				EventBusUtil.fireEvent(new MemberAccountSelectEvent(returnData));
			}
		}));
	}
	
	@Override
	protected ModelKeyProvider<MemberAccountRowDto> getModelKeyProvider() {
		return new ModelKeyProvider<MemberAccountRowDto>(){
			@Override
			public String getKey(MemberAccountRowDto item) {
				return item.getMemberAccountId().toString();
			}
		};
	}
	
	interface MemberAccountProperties extends PropertyAccess<MemberAccountRowDto> {
		
		ValueProvider<MemberAccountRowDto, Integer> memberAccountId();
		ValueProvider<MemberAccountRowDto, String> loginId();
		ValueProvider<MemberAccountRowDto, Double> accountValue();
	}

	@Override
	protected void initColumnConfig(List<ColumnConfig<MemberAccountRowDto, ?>> configList) {
		MemberAccountProperties props = GWT.create(MemberAccountProperties.class);
		
		ColumnConfigEx<MemberAccountRowDto, String> nmColumn = new ColumnConfigEx<MemberAccountRowDto, String>(props.loginId(), 50, "会员用户", HasHorizontalAlignment.ALIGN_CENTER, false);
		nmColumn.setCell(new AbstractCell<String>() {
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context,	String value, SafeHtmlBuilder sb) {
				MemberAccountRowDto memberAccountRowDto = store.get(context.getIndex());
				sb.appendEscaped(memberAccountRowDto.getLoginId() +"("+ memberAccountRowDto.getUserNm() + ")");
			}
		});
		configList.add(nmColumn);
		configList.add(new ColumnConfigEx<MemberAccountRowDto, Double>(props.accountValue(), 24, "余额", HasHorizontalAlignment.ALIGN_CENTER, false));
	}
	
	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		statusTabPanel.addSelectionHandler(new SelectionHandler<Widget>() {
			@Override
			public void onSelection(SelectionEvent<Widget> event) {
				int index = statusTabPanel.getWidgetIndex(event.getSelectedItem());
				acCodeEnum = AccountTypeCodeEnum.fromCode(String.valueOf(index));
				isSelect = true;
				loader.load();
			}
		});
		grid.getSelectionModel().addSelectionHandler(new SelectionHandler<MemberAccountRowDto>() {
			@Override
			public void onSelection(SelectionEvent<MemberAccountRowDto> event) {
				MemberAccountRowDto selectedItem = event.getSelectedItem();
				isSelect = false;
				EventBusUtil.fireEvent(new MemberAccountSelectEvent(selectedItem == null ? null : selectedItem));
			}
		});
		grid.addCellDoubleClickHandler(new CellDoubleClickHandler(){
			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				chargeButton.fireEvent(new SelectEvent());
			}
		});
		return widget;
	}

	@Override
	public void onModuleReload(BodyContentEvent event) {
		super.onModuleReload(event);
		// 运营商登陆
		if (Global.PLATFORM_ORG_ID != MainFrame.getAdminUser().getOrgId()) {
			orgCombobox.hide();
		} else {
			orgCombobox.reset();
			orgCombobox.addValueChangeHandler(new ValueChangeHandler<Integer>() {
				@Override
				public void onValueChange(ValueChangeEvent<Integer> event) {
					loader.load();
				}
			});
		}
	}
	
	@Override
	protected CallBack<MemberAccountRowDto> getRestReq() {
		return new CallBack<MemberAccountRowDto>(){
			@Override
			public void call(int offset, int limit,final RestyGwtPagingLoader<MemberAccountRowDto> loader) {
				MemberAccountListReq memberAccountListReq = new MemberAccountListReq();
				memberAccountListReq.setSearchText(searchField.getValue());
				memberAccountListReq.setAccountTypeCode(acCodeEnum.toCode());
				// 运营商登陆
				if (Global.PLATFORM_ORG_ID != MainFrame.getAdminUser().getOrgId()) {
					memberAccountListReq.setOrgId(MainFrame.getAdminUser().getOrgId());
				} else {
					memberAccountListReq.setOrgId(orgCombobox.getValue());
				}
				ClientManager.getMemberAccountClient().findList(memberAccountListReq, offset, limit, new RestCallback<ClientPage<MemberAccountRowDto>>(){

					@Override
					public void onSuccess(Method method, ClientPage<MemberAccountRowDto> response) {
						loader.onLoad(response.getList(), response.getTotalLength(), response.getOffset());
						// 默认选择第一条数据
						if (response.getList().size() != 0 && isSelect) {
							grid.getSelectionModel().select(0, false);
							EventBusUtil.fireEvent(new MemberAccountSelectEvent(response.getList().get(0)));
							isSelect = false;
						}
					}
				});
			}
		};
	}
}
