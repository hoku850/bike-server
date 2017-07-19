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
import org.ccframe.client.commons.WindowEventCallback;
import org.ccframe.client.components.CcEnumCombobox;
import org.ccframe.client.components.CcLabelValueCombobox;
import org.ccframe.client.module.core.event.BodyContentEvent;
import org.ccframe.client.module.core.event.LoadWindowEvent;
import org.ccframe.client.module.core.view.MainFrame;
import org.ccframe.subsys.bike.domain.code.FixStatCodeEnum;
import org.ccframe.subsys.bike.domain.code.RepairResonCodeEnum;
import org.ccframe.subsys.bike.domain.entity.UserToRepairRecord;
import org.ccframe.subsys.bike.dto.UserToRepairRecordListReq;
import org.ccframe.subsys.bike.dto.UserToRepairRecordRowDto;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
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
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

@Singleton
public class UserToRepairRecordListView extends BasePagingListView<UserToRepairRecordRowDto>{

	interface UserToRepairRecordListUiBinder extends UiBinder<Component, UserToRepairRecordListView> {}
	private static UserToRepairRecordListUiBinder uiBinder = GWT.create(UserToRepairRecordListUiBinder.class);
	
	private Boolean isAgent = false;
	
	@UiField
	CcLabelValueCombobox orgId;
	
	@UiField
	CcEnumCombobox fixStatCode;
	
	@UiField
	TextField searchText;
	
	@UiField
	TextButton searchButton;
	
	@UiHandler("searchButton")
	public void searchButtonClick(SelectEvent e){
		loader.load();
	}
	
	@Override
	protected ModelKeyProvider<UserToRepairRecordRowDto> getModelKeyProvider() {
		return new ModelKeyProvider<UserToRepairRecordRowDto>(){
			@Override
			public String getKey(UserToRepairRecordRowDto item) {
				return item.getUserToRepairRecordId().toString();
			}
		};
	}
	
	interface UserToRepairRecordProperties extends PropertyAccess<UserToRepairRecordRowDto> {
		
		ValueProvider<UserToRepairRecordRowDto, String> orgNm();
		ValueProvider<UserToRepairRecordRowDto, String> ifFinishFix();
		ValueProvider<UserToRepairRecordRowDto, Integer> userId();
		ValueProvider<UserToRepairRecordRowDto, String> lockerHardwareCode();
		ValueProvider<UserToRepairRecordRowDto, String> bikePlateNumber();
		ValueProvider<UserToRepairRecordRowDto, String> toRepairLocationCode();
		ValueProvider<UserToRepairRecordRowDto, String> toRepairTimeStr();
		ValueProvider<UserToRepairRecordRowDto, String> finishFixTimeStr();
	}
	
	
	@Override
	protected void initColumnConfig(List<ColumnConfig<UserToRepairRecordRowDto, ?>> configList) {
		UserToRepairRecordProperties props = GWT.create(UserToRepairRecordProperties.class);
		
		ColumnConfigEx<UserToRepairRecordRowDto, String> fixStatColumn = new ColumnConfigEx<UserToRepairRecordRowDto, String>(props.ifFinishFix(), 80, "维修状态", HasHorizontalAlignment.ALIGN_CENTER, true);
		fixStatColumn.setCell(new AbstractCell<String>(){
            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context,
                    String value, SafeHtmlBuilder sb) {
            	UserToRepairRecordRowDto userToRepairRecordRowDto = store.get(context.getIndex());
                switch(FixStatCodeEnum.fromCode(userToRepairRecordRowDto.getIfFinishFix())){
                case UNFIX:
                    sb.appendHtmlConstant("未处理");
                    break;
                case FIXED:
                    sb.appendHtmlConstant("已处理");
                    break;
                default:
                    sb.appendHtmlConstant("---");
                    break;
                }
            }
        });
		
		ColumnConfigEx<UserToRepairRecordRowDto, String> repairLocationCodeColumn = new ColumnConfigEx<UserToRepairRecordRowDto, String>(props.toRepairLocationCode(), 100, "报修原因", HasHorizontalAlignment.ALIGN_CENTER, false);
		repairLocationCodeColumn.setCell(new AbstractCell<String>(){
            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context,
                    String value, SafeHtmlBuilder sb) {
            	UserToRepairRecordRowDto userToRepairRecordRowDto = store.get(context.getIndex());
                switch(RepairResonCodeEnum.fromCode(userToRepairRecordRowDto.getToRepairLocationCode())){
                case A:
                    sb.appendHtmlConstant("坐垫");
                    break;
                case B:
                    sb.appendHtmlConstant("车锁");
                    break;
                case C:
                    sb.appendHtmlConstant("链条");
                    break;
                case D:
                    sb.appendHtmlConstant("车把");
                    break;
                case E:
                    sb.appendHtmlConstant("刹车");
                    break;
                case F:
                    sb.appendHtmlConstant("轮胎");
                    break;
                default:
                    sb.appendHtmlConstant("---");
                    break;
                }
            }
        });
		
		configList.add(new ColumnConfigEx<UserToRepairRecordRowDto, String>(props.orgNm(), 100, "运营商", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(fixStatColumn);
		configList.add(new ColumnConfigEx<UserToRepairRecordRowDto, Integer>(props.userId(), 120, "登录ID", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<UserToRepairRecordRowDto, String>(props.lockerHardwareCode(), 150, "智能锁硬件编号", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<UserToRepairRecordRowDto, String>(props.bikePlateNumber(), 150, "单车车牌号", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(repairLocationCodeColumn);
		configList.add(new ColumnConfigEx<UserToRepairRecordRowDto, String>(props.toRepairTimeStr(), 160, "报修时间", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<UserToRepairRecordRowDto, String>(props.finishFixTimeStr(), 160, "维修时间", HasHorizontalAlignment.ALIGN_CENTER, true));
	}
	
	@Override
	protected CallBack<UserToRepairRecordRowDto> getRestReq() {
		return new CallBack<UserToRepairRecordRowDto>(){
			@Override
			public void call(int offset, int limit,final RestyGwtPagingLoader<UserToRepairRecordRowDto> loader) {
				UserToRepairRecordListReq userToRepairRecordListReq = new UserToRepairRecordListReq();
				userToRepairRecordListReq.setSearchText(searchText.getValue());
				userToRepairRecordListReq.setOrgId(orgId.getValue());
				userToRepairRecordListReq.setFixStatCode(fixStatCode.getValue()==""?null:fixStatCode.getValue());
				
				if(isAgent){
					userToRepairRecordListReq.setOrgId(MainFrame.adminUser.getOrgId());
//					userToRepairRecordListReq.setOrgId(502);
				}else{
					userToRepairRecordListReq.setOrgId(orgId.getValue());
				}
				
				ClientManager.getUserToRepairRecordClient().findList(userToRepairRecordListReq, offset, limit, new RestCallback<ClientPage<UserToRepairRecordRowDto>>(){
					@Override
					public void onSuccess(Method method, ClientPage<UserToRepairRecordRowDto> response) {
						loader.onLoad(response.getList(), response.getTotalLength(), response.getOffset());
					}
				});
				
			}
		};
	}
	
	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		if(MainFrame.adminUser.getOrgId() != Global.PLATFORM_ORG_ID){
			isAgent = true;
		}else{
			isAgent = false;
		}
		if(isAgent){
			this.columnModel.getColumn(0).setHidden(true);
			this.orgId.hide();
		}
		grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			
			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				UserToRepairRecordRowDto selectedItem = grid.getSelectionModel().getSelectedItem();
				
				EventBusUtil.fireEvent(new LoadWindowEvent<Integer, UserToRepairRecord, EventHandler>(
						ViewResEnum.USER_TO_REPAIR_WINDOW, 
						selectedItem.getUserToRepairRecordId(),
						new WindowEventCallback<UserToRepairRecord>(){
							@Override
							public void onClose(UserToRepairRecord returnData) {
								loader.load(); //保存完毕后刷新，可能有过滤条件，因此采用刷新方式
							}
						}
				));
			}
		});
		return widget;
	}
	
	@Override
	public void onModuleReload(BodyContentEvent event) {
		super.onModuleReload(event);
		if(MainFrame.adminUser.getOrgId() != Global.PLATFORM_ORG_ID){
			isAgent = true;
		}else{
			isAgent = false;
		}
		if(isAgent){
			this.columnModel.getColumn(0).setHidden(true);
			this.orgId.hide();
		}
		fixStatCode.reset();
		orgId.reset();
		orgId.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				loader.load();
			}
		});
		fixStatCode.addValueChangeHandler(new ValueChangeHandler<String>(){
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				loader.load();
			}
		});
	}
}