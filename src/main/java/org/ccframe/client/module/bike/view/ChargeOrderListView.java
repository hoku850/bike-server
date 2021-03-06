package org.ccframe.client.module.bike.view;

import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.base.BasePagingListView;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.client.commons.ColumnConfigEx;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.RestyGwtPagingLoader;
import org.ccframe.client.commons.RestyGwtPagingLoader.CallBack;
import org.ccframe.client.components.CcEnumCombobox;
import org.ccframe.client.components.CcLabelValueCombobox;
import org.ccframe.client.module.core.event.BodyContentEvent;
import org.ccframe.client.module.core.view.MainFrame;
import org.ccframe.subsys.bike.dto.ChargeOrderListReq;
import org.ccframe.subsys.bike.dto.ChargeOrderRowDto;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

@Singleton
public class ChargeOrderListView extends BasePagingListView<ChargeOrderRowDto> {

	interface ChargeOrderListUiBinder extends UiBinder<Component, ChargeOrderListView> {	}
	private static ChargeOrderListUiBinder uiBinder = GWT.create(ChargeOrderListUiBinder.class);

    @UiField
    public CcLabelValueCombobox orgCombobox;
    
    @UiField
    public CcEnumCombobox chargeStateCombobox;

    @UiField
    public CcEnumCombobox payStateCombobox;
    
	@UiField
    public TextField searchField;
    
	@UiHandler("searchButton")
	public void searchButtonClick(SelectEvent e){
		loader.load();
	}
	
	@UiHandler("exportButton")
	public void exportButtonClick(SelectEvent e){
		//excel下载用
        ClientManager.getChargeOrderClient().exportUrl(MainFrame.getAdminUser().getOrgId(), new RestCallback<String>(){			
			@Override
			public void onSuccess(Method method, String response) {
				Window.open(GWT.getHostPageBaseURL() + response, "", "");
			}
		});
	}
	
	@Override
	protected ModelKeyProvider<ChargeOrderRowDto> getModelKeyProvider() {
		return new ModelKeyProvider<ChargeOrderRowDto>(){
			@Override
			public String getKey(ChargeOrderRowDto item) {
				return item.getChargeOrderId().toString();
			}
		};
	}
	
	interface BikeTypeProperties extends PropertyAccess<ChargeOrderRowDto> {
		
		ValueProvider<ChargeOrderRowDto, Integer> chargeOrderId();
		ValueProvider<ChargeOrderRowDto, String> chargeOrderNum();
		ValueProvider<ChargeOrderRowDto, String> orgNm();
		ValueProvider<ChargeOrderRowDto, String> loginId();
		ValueProvider<ChargeOrderRowDto, String> paymentTypeCodeStr();
		ValueProvider<ChargeOrderRowDto, Double> chargeAmmount();
		ValueProvider<ChargeOrderRowDto, String> paymentTransactionalNumber();
		ValueProvider<ChargeOrderRowDto, String> chargeOrderStatCodeStr();
		ValueProvider<ChargeOrderRowDto, String> createTimeStr();
		ValueProvider<ChargeOrderRowDto, String> chargeFinishTimeStr();
	}
	
	@Override
	protected void initColumnConfig(List<ColumnConfig<ChargeOrderRowDto, ?>> configList) {
		BikeTypeProperties props = GWT.create(BikeTypeProperties.class);
		
		configList.add(new ColumnConfigEx<ChargeOrderRowDto, String>(props.chargeOrderNum(), 150, "订单号码", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<ChargeOrderRowDto, String>(props.orgNm(), 100, "运营商", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<ChargeOrderRowDto, String>(props.loginId(), 130, "登陆ID", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<ChargeOrderRowDto, String>(props.paymentTypeCodeStr(), 100, "支付类型", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<ChargeOrderRowDto, Double>(props.chargeAmmount(), 100, "金额（元）", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<ChargeOrderRowDto, String>(props.paymentTransactionalNumber(), 150, "交易流水", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<ChargeOrderRowDto, String>(props.chargeOrderStatCodeStr(), 80, "状态", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<ChargeOrderRowDto, String>(props.createTimeStr(), 130, "创建时间", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<ChargeOrderRowDto, String>(props.chargeFinishTimeStr(), 130, "完成时间", HasHorizontalAlignment.ALIGN_CENTER, true));
	}

	@Override
	protected CallBack<ChargeOrderRowDto> getRestReq() {
		return new CallBack<ChargeOrderRowDto>(){
			@Override
			public void call(int offset, int limit,final RestyGwtPagingLoader<ChargeOrderRowDto> loader) {
				ChargeOrderListReq chargeOrderListReq = new ChargeOrderListReq();
				if (Global.PLATFORM_ORG_ID != MainFrame.getAdminUser().getOrgId()) {
					chargeOrderListReq.setOrgId(MainFrame.getAdminUser().getOrgId());
				} else {
					chargeOrderListReq.setOrgId(orgCombobox.getValue());
				}
				chargeOrderListReq.setChargeOrderStatCode(chargeStateCombobox.getValue());
				chargeOrderListReq.setPaymentTypeCode(payStateCombobox.getValue());
				chargeOrderListReq.setSearchText(searchField.getValue());
				ClientManager.getChargeOrderClient().findList(chargeOrderListReq, offset, limit, new RestCallback<ClientPage<ChargeOrderRowDto>>(){
					@Override
					public void onSuccess(Method method, ClientPage<ChargeOrderRowDto> response) {
						loader.onLoad(response.getList(), response.getTotalLength(), response.getOffset());
					}
				});
			}
		};
	}

	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		chargeStateCombobox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				loader.load();
			}
		});
		payStateCombobox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				loader.load();
			}
		});
		return widget;
	}

	@Override
	public void onModuleReload(BodyContentEvent event) {
		super.onModuleReload(event);
		// 运营商登陆
		if (Global.PLATFORM_ORG_ID != MainFrame.getAdminUser().getOrgId()) {
			this.orgCombobox.hide();
			this.columnModel.getColumn(1).setHidden(true);
			this.view.getHeader().refresh(); //强制更新头部
		} else {
			orgCombobox.reset();
			orgCombobox.addValueChangeHandler(new ValueChangeHandler<Integer>() {
				@Override
				public void onValueChange(ValueChangeEvent<Integer> event) {
					loader.load();
				}
			});
		}	
		// 重置过滤条件
		chargeStateCombobox.reset();
		payStateCombobox.reset();
		searchField.setValue(null);
	}
}
