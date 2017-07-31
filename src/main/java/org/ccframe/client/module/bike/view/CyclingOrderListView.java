package org.ccframe.client.module.bike.view;

import java.util.Date;
import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.ViewResEnum;
import org.ccframe.client.base.BasePagingListView;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.ColumnConfigEx;
import org.ccframe.client.commons.EventBusUtil;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.RestyGwtPagingLoader;
import org.ccframe.client.commons.RestyGwtPagingLoader.CallBack;
import org.ccframe.client.commons.UtilDateTimeClient;
import org.ccframe.client.commons.ViewUtil;
import org.ccframe.client.commons.WindowEventCallback;
import org.ccframe.client.components.CcDateField;
import org.ccframe.client.components.CcEnumCombobox;
import org.ccframe.client.components.CcLabelValueCombobox;
import org.ccframe.client.module.core.event.BodyContentEvent;
import org.ccframe.client.module.core.event.LoadWindowEvent;
import org.ccframe.client.module.core.view.MainFrame;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.dto.CyclingOrderClientPage;
import org.ccframe.subsys.bike.dto.CyclingOrderListReq;
import org.ccframe.subsys.bike.dto.CyclingOrderRowDto;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
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
public class CyclingOrderListView extends BasePagingListView<CyclingOrderRowDto> {

	interface CyclingOrderListUiBinder extends UiBinder<Component, CyclingOrderListView> {	}
	private static CyclingOrderListUiBinder uiBinder = GWT.create(CyclingOrderListUiBinder.class);
	
	private static final long ONE_MONTH = (long)30 * 24 * 60 * 60 * 1000;
	
    @UiField
    public CcLabelValueCombobox orgCombobox;
    
    @UiField
    public CcLabelValueCombobox bikeTypeCombobox;  
    
    @UiField
    public CcEnumCombobox orderStateCombobox;
	
	@UiField
	public CcDateField startTime;
	
	@UiField
	public CcDateField endTime;
	
	@UiField
	public TextField searchField;
	
	@UiField
	public Label totalOrderAmmount;
	
   @UiField
    public TextButton findTrajectory;
	
	@UiHandler("searchButton")
	public void searchButtonClick(SelectEvent e){
		loader.load();
	}
	
	@UiHandler("exportButton")
	public void exportButtonClick(SelectEvent e){
		//excel下载用
        ClientManager.getCyclingOrderClient().exportUrl(MainFrame.adminUser.getOrgId(), new RestCallback<String>(){			
			@Override
			public void onSuccess(Method method, String response) {
				Window.open(GWT.getHostPageBaseURL() + response, "", "");
			}
		});
	}
	
	@UiHandler("findTrajectory")
	public void findTrajectory(SelectEvent e){
		if(grid.getSelectionModel().getSelectedItems().size() != 1){
			ViewUtil.error("系统信息", "请选择一条记录进行编辑");
			return;
		}
		CyclingOrderRowDto selectedItem = grid.getSelectionModel().getSelectedItem();
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, CyclingOrderRowDto, EventHandler>(
				ViewResEnum.CYCLING_ORDER_WINDOW, 
				selectedItem.getCyclingOrderId(), 
				new WindowEventCallback<CyclingOrderRowDto>(){
					@Override
					public void onClose(CyclingOrderRowDto returnData) {
						loader.load(); //保存完毕后刷新
					}
		}));
	}
	
	@Override
	protected ModelKeyProvider<CyclingOrderRowDto> getModelKeyProvider() {
		return new ModelKeyProvider<CyclingOrderRowDto>(){
			@Override
			public String getKey(CyclingOrderRowDto item) {
				return item.getCyclingOrderId().toString();
			}
		};
	}
	
	interface BikeTypeProperties extends PropertyAccess<CyclingOrderRowDto> {
		
		ValueProvider<CyclingOrderRowDto, Integer> cyclingOrderId();
		ValueProvider<CyclingOrderRowDto, String> orgNm();
		ValueProvider<CyclingOrderRowDto, String> loginId();
		ValueProvider<CyclingOrderRowDto, String> lockerHardwareCode();
		ValueProvider<CyclingOrderRowDto, String> bikePlateNumber();
		ValueProvider<CyclingOrderRowDto, String> bikeTypeNm();
		ValueProvider<CyclingOrderRowDto, String> cyclingOrderStatCodeStr();
		ValueProvider<CyclingOrderRowDto, Double> orderAmmount();
		ValueProvider<CyclingOrderRowDto, String> endTimeStr();
	}
	
	@Override
	protected void initColumnConfig(List<ColumnConfig<CyclingOrderRowDto, ?>> configList) {
		BikeTypeProperties props = GWT.create(BikeTypeProperties.class);
		
		configList.add(new ColumnConfigEx<CyclingOrderRowDto, Integer>(props.cyclingOrderId(), 130, "订单编号", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<CyclingOrderRowDto, String>(props.orgNm(), 100, "运营商", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<CyclingOrderRowDto, String>(props.loginId(), 120, "登陆ID", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<CyclingOrderRowDto, String>(props.lockerHardwareCode(), 150, "智能锁硬件编号", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<CyclingOrderRowDto, String>(props.bikePlateNumber(), 150, "单车车牌号", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<CyclingOrderRowDto, String>(props.bikeTypeNm(), 100, "单车类型", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<CyclingOrderRowDto, String>(props.cyclingOrderStatCodeStr(), 80, "状态", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<CyclingOrderRowDto, Double>(props.orderAmmount(), 80, "金额（元）", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<CyclingOrderRowDto, String>(props.endTimeStr(), 130, "结束时间", HasHorizontalAlignment.ALIGN_CENTER, true));
	}

	@Override
	protected CallBack<CyclingOrderRowDto> getRestReq() {
		return new CallBack<CyclingOrderRowDto>(){
			@Override
			public void call(int offset, int limit,final RestyGwtPagingLoader<CyclingOrderRowDto> loader) {
				CyclingOrderListReq cyclingOrderListReq = new CyclingOrderListReq();
				if (Global.PLATFORM_ORG_ID != MainFrame.adminUser.getOrgId()) {
					cyclingOrderListReq.setOrgId(MainFrame.adminUser.getOrgId());
				} else {
					cyclingOrderListReq.setOrgId(orgCombobox.getValue());
				}
				cyclingOrderListReq.setBikeTypeId(bikeTypeCombobox.getValue());
				cyclingOrderListReq.setOrderState(orderStateCombobox.getValue());
				cyclingOrderListReq.setStartTimeStr(startTime.getValue());
				cyclingOrderListReq.setEndTimeStr(endTime.getValue());
				cyclingOrderListReq.setSearchField(searchField.getValue());
				ClientManager.getCyclingOrderClient().findList(cyclingOrderListReq, offset, limit, new RestCallback<CyclingOrderClientPage<CyclingOrderRowDto>>(){

					@Override
					public void onSuccess(Method method, CyclingOrderClientPage<CyclingOrderRowDto> response) {
						loader.onLoad(response.getList(), response.getTotalLength(), response.getOffset());
						totalOrderAmmount.setText("总计：" + response.getCyclingOrderTotalAmount() + "元");
					}
				});
			}
		};
	}

	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		bikeTypeCombobox.addValueChangeHandler(new ValueChangeHandler<Integer>() {
			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				loader.load();
			}
		});
		orderStateCombobox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				loader.load();
			}
		});
		grid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			@Override
			public void onCellClick(CellDoubleClickEvent event) {
				findTrajectory.fireEvent(new SelectEvent());
			}
		});
		return widget;
	}

	@Override
	public void onModuleReload(BodyContentEvent event) {
		super.onModuleReload(event);
		// 时间范围默认为当前至前30天
		startTime.setValue(UtilDateTimeClient.convertDateTimeToString(new Date(new Date().getTime() - ONE_MONTH)));
		endTime.setValue(UtilDateTimeClient.convertDateTimeToString(new Date()));
		// 运营商登陆
		if (Global.PLATFORM_ORG_ID != MainFrame.adminUser.getOrgId()) {
			this.columnModel.getColumn(1).setHidden(true);
			this.orgCombobox.hide();
			bikeTypeCombobox.setExtraParam(MainFrame.adminUser.getOrgId().toString());
			bikeTypeCombobox.reset();
		} else {
			orgCombobox.reset();
			bikeTypeCombobox.setExtraParam(Integer.toString(Global.COMBOBOX_ALL_VALUE));
			bikeTypeCombobox.reset();
			orgCombobox.addValueChangeHandler(new ValueChangeHandler<Integer>() {
				@Override
				public void onValueChange(ValueChangeEvent<Integer> event) {
					bikeTypeCombobox.setExtraParam(event.getValue().toString());
					bikeTypeCombobox.reset();
					loader.load();
				}
			});
		}
		// 重置过滤条件
		orderStateCombobox.reset();
	}
	
	@UiHandler("finishButton")
	protected void doFinishClick(SelectEvent e) {
		if(grid.getSelectionModel().getSelectedItems().size() != 1){
			ViewUtil.error("系统信息", "请选择一条记录进行编辑");
			return;
		}
		CyclingOrderRowDto selectedRow = grid.getSelectionModel().getSelectedItem();
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, CyclingOrder, EventHandler>(
				ViewResEnum.CYCLING_ORDER_FINISH_WINDOW,
				selectedRow.getCyclingOrderId(), 
				new WindowEventCallback<CyclingOrder>(){
			@Override
			public void onClose(CyclingOrder returnData) {
				loader.load(); //保存完毕后刷新，可能有过滤条件，因此采用刷新方式
			}
		}));
		
	}
}
