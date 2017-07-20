package org.ccframe.client.module.bike.view;

import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.ViewResEnum;
import org.ccframe.client.base.BaseCrudListView;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.client.commons.ColumnConfigEx;
import org.ccframe.client.commons.EventBusUtil;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.RestyGwtPagingLoader;
import org.ccframe.client.commons.ViewUtil;
import org.ccframe.client.commons.WindowEventCallback;
import org.ccframe.client.commons.RestyGwtPagingLoader.CallBack;
import org.ccframe.client.components.CcEnumCombobox;
import org.ccframe.client.components.CcLabelValueCombobox;
import org.ccframe.client.module.core.event.BodyContentEvent;
import org.ccframe.client.module.core.event.LoadWindowEvent;
import org.ccframe.client.module.core.view.MainFrame;
import org.ccframe.subsys.bike.domain.code.SmartLockStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.dto.SmartLockListReq;
import org.ccframe.subsys.bike.dto.SmartLockRowDto;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.info.Info;

@Singleton
public class SmartLockListView extends BaseCrudListView<SmartLockRowDto>{

	interface SmartLockListUiBinder extends UiBinder<Component, SmartLockListView> {}
	private static SmartLockListUiBinder uiBinder = GWT.create(SmartLockListUiBinder.class);
	
	private Boolean isAgent = false;
	
	@UiField
    public TextField searchText;
	
	@UiField
    public CcEnumCombobox smartLockStatCode;
    
    @UiField
    public CcLabelValueCombobox orgId;
	
	@UiField
	TextButton grantButton;
	
	@UiField
	TextButton importButton;
	
	@UiField
	TextButton desertButton;
	
	@UiField
	TextButton exportButton;
	
	@UiField
	TextButton searchButton;
	
	@UiHandler("grantButton")
	public void grantButtonClick(SelectEvent e){
		EventBusUtil.fireEvent(new LoadWindowEvent<Void, Void, EventHandler>(
			ViewResEnum.SMART_LOCK_GRANT_WINDOW,
			null,
			new WindowEventCallback<Void>(){
				@Override
				public void onClose(Void returnData) {
					loader.load();
				}
			}
		));
	}
	
	@UiHandler("importButton")
	public void importButtonClick(SelectEvent e){
		EventBusUtil.fireEvent(new LoadWindowEvent<Void, Void, EventHandler>(
			ViewResEnum.SMART_LOCK_IMPORT_WINDOW,
			null,
			new WindowEventCallback<Void>(){
				@Override
				public void onClose(Void returnData) {
					loader.load();
				}
			}
		));
	}
	
	@UiHandler("exportButton")
	public void exportButtonClick(SelectEvent e){
		//excel下载用
        ClientManager.getSmartLockClient().exportUrl(MainFrame.adminUser.getOrgId(), new RestCallback<String>(){			
			@Override
			public void onSuccess(Method method, String response) {
				Window.open(GWT.getHostPageBaseURL() + response, "", "");
			}
		});
	}
	
	@UiHandler("desertButton")
	public void desertButtonClick(SelectEvent e){
		final SmartLockRowDto selectedRow = grid.getSelectionModel().getSelectedItem();
		
		ViewUtil.confirm("提示信息", "你确定要报废该智能锁吗？ 该操作将不可撤销！！", new Runnable(){
			@Override
			public void run() {
				ClientManager.getSmartLockClient().doDesert(selectedRow, new RestCallback<Void>(){
					@Override
					public void onSuccess(Method method, Void response) {
						Info.display("操作完成", "智能锁报废成功！");
						loader.load();
					}
				});
				
			}
		});
	}
	
	@UiHandler("searchButton")
	public void searchButtonClick(SelectEvent e){
		loader.load();
	}
	
	
	@Override
	public void handleAddClick(SelectEvent e) {
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, SmartLock, EventHandler>(ViewResEnum.SMART_LOCK_WINDOW, null, new WindowEventCallback<SmartLock>(){
			@Override
			public void onClose(SmartLock returnData) {
				loader.load(); //保存完毕后刷新
			}
		}));
	}
	
	@Override
	protected void onEditClick(SelectEvent e) {
		SmartLockRowDto selectedRow = grid.getSelectionModel().getSelectedItem();
		
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, SmartLock, EventHandler>(
				ViewResEnum.SMART_LOCK_WINDOW,
				selectedRow.getSmartLockId(), 
				new WindowEventCallback<SmartLock>(){
			@Override
			public void onClose(SmartLock returnData) {
				loader.load(); //保存完毕后刷新，可能有过滤条件，因此采用刷新方式
			}
		}));
	}
	
	@Override
	protected void doDeleteClick(SelectEvent e) {
		ClientManager.getSmartLockClient().delete(grid.getSelectionModel().getSelectedItem().getSmartLockId(), new RestCallback<Void>(){
			@Override
			public void onSuccess(Method method, Void response) {
				Info.display("操作完成", "智能锁删除成功");
				loader.load(); //保存完毕后刷新，可能有过滤条件，因此采用刷新方式
			}
		});
	}
	
	@Override
	protected ModelKeyProvider<SmartLockRowDto> getModelKeyProvider() {
		return new ModelKeyProvider<SmartLockRowDto>(){
			@Override
			public String getKey(SmartLockRowDto item) {
				return item.getSmartLockId().toString();
			}
		};
	}
	
	interface SmartLockProperties extends PropertyAccess<SmartLockRowDto> {
		
		ValueProvider<SmartLockRowDto, String> lockerHardwareCode();
		ValueProvider<SmartLockRowDto, String> imeiCode();
		ValueProvider<SmartLockRowDto, String> macAddress();
		ValueProvider<SmartLockRowDto, String> orgNm();
		ValueProvider<SmartLockRowDto, String> bikePlateNumber();
		ValueProvider<SmartLockRowDto, String> bikeTypeNm();
		ValueProvider<SmartLockRowDto, String> smartLockStatCode();
		ValueProvider<SmartLockRowDto, String> activeDateStr();
		ValueProvider<SmartLockRowDto, String> lastUseDateStr();
	}
	
	
	@Override
	protected void initColumnConfig(List<ColumnConfig<SmartLockRowDto, ?>> configList) {
		SmartLockProperties props = GWT.create(SmartLockProperties.class);
		
		ColumnConfigEx<SmartLockRowDto, String> smartLockStatColumn = new ColumnConfigEx<SmartLockRowDto, String>(props.smartLockStatCode(), 80, "状态", HasHorizontalAlignment.ALIGN_CENTER, true);
		smartLockStatColumn.setCell(new AbstractCell<String>(){
            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context,
                    String value, SafeHtmlBuilder sb) {
            	SmartLockRowDto smartLockRowDto = store.get(context.getIndex());
                switch(SmartLockStatCodeEnum.fromCode(smartLockRowDto.getSmartLockStatCode())){
                case ACTIVED:
                    sb.appendHtmlConstant("已激活");
                    break;
                case DESERTED:
                    sb.appendHtmlConstant("已废弃");
                    break;
                case GRANTED:
                    sb.appendHtmlConstant("已发放");
                    break;
                case PRODUCED:
                    sb.appendHtmlConstant("已生产");
                    break;
                case TO_FIX:
                    sb.appendHtmlConstant("维修中");
                    break;
                case UNPRODUCE:
                    sb.appendHtmlConstant("未生产");
                    break;
                default:
                    sb.appendHtmlConstant("---");
                    break;
                }
            }
        });
		
		configList.add(new ColumnConfigEx<SmartLockRowDto, String>(props.lockerHardwareCode(), 150, "硬件编号", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<SmartLockRowDto, String>(props.imeiCode(), 150, "IMEI码", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<SmartLockRowDto, String>(props.macAddress(), 150, "MAC地址", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(smartLockStatColumn);
		configList.add(new ColumnConfigEx<SmartLockRowDto, String>(props.orgNm(), 100, "运营商", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<SmartLockRowDto, String>(props.bikePlateNumber(), 150, "单车车牌号", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<SmartLockRowDto, String>(props.bikeTypeNm(), 100, "单车类型", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<SmartLockRowDto, String>(props.activeDateStr(), 130, "激活日期", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<SmartLockRowDto, String>(props.lastUseDateStr(), 130, "最后使用日期", HasHorizontalAlignment.ALIGN_CENTER, true));
	}
	
	@Override
	protected CallBack<SmartLockRowDto> getRestReq() {
		return new CallBack<SmartLockRowDto>(){
			@Override
			public void call(int offset, int limit,final RestyGwtPagingLoader<SmartLockRowDto> loader) {
				SmartLockListReq smartLockListReq = new SmartLockListReq();
				
				smartLockListReq.setSearchText(searchText.getValue());
				smartLockListReq.setSmartLockStatCode(smartLockStatCode.getValue()==""?null:smartLockStatCode.getValue());
				
				if(isAgent){
					smartLockListReq.setOrgId(MainFrame.adminUser.getOrgId());
//					smartLockListReq.setOrgId(503);
				}else{
					smartLockListReq.setOrgId(orgId.getValue());
				}
				ClientManager.getSmartLockClient().findSmartLockList(smartLockListReq, offset, limit, new RestCallback<ClientPage<SmartLockRowDto>>(){
					@Override
					public void onSuccess(Method method, ClientPage<SmartLockRowDto> response) {
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
			this.columnModel.getColumn(4).setHidden(true);
			this.addButton.hide();
			this.editButton.hide();
			this.deleteButton.hide();
			this.grantButton.hide();
			this.importButton.hide();
			this.orgId.hide();
		}else{
			this.desertButton.hide();
		}
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
			this.columnModel.getColumn(4).setHidden(true);
			this.addButton.hide();
			this.editButton.hide();
			this.deleteButton.hide();
			this.grantButton.hide();
			this.importButton.hide();
			this.orgId.hide();
		}else{
			this.desertButton.hide();
		}
		
		orgId.reset();
		smartLockStatCode.reset();
		orgId.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				loader.load();
			}
		});
		smartLockStatCode.addValueChangeHandler(new ValueChangeHandler<String>(){
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				loader.load();
			}
		});

			
		
	}
}
