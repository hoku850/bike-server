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
import org.ccframe.client.commons.RestyGwtPagingLoader.CallBack;
import org.ccframe.client.commons.WindowEventCallback;
import org.ccframe.client.components.CcLabelValueCombobox;
import org.ccframe.client.module.core.event.BodyContentEvent;
import org.ccframe.client.module.core.event.LoadWindowEvent;
import org.ccframe.client.module.core.view.MainFrame;
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.dto.BikeTypeListReq;
import org.ccframe.subsys.bike.dto.BikeTypeRowDto;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.info.Info;

@Singleton
public class BikeTypeListView extends BaseCrudListView<BikeTypeRowDto> {

	interface BikeTypeListUiBinder extends UiBinder<Component, BikeTypeListView> {	}
	private static BikeTypeListUiBinder uiBinder = GWT.create(BikeTypeListUiBinder.class);

    @UiField
    public CcLabelValueCombobox orgCombobox;
    
	@Override
	public void handleAddClick(SelectEvent e) {
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, BikeType, EventHandler>(ViewResEnum.BIKE_TYPE_WINDOW, null, new WindowEventCallback<BikeType>(){
			@Override
			public void onClose(BikeType returnData) {
				loader.load(); //保存完毕后刷新
			}
		}));
	}

	@Override
	protected void onEditClick(SelectEvent e) {
		BikeTypeRowDto BikeTypeRowDto = grid.getSelectionModel().getSelectedItem();
		
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, BikeType, EventHandler>(
				ViewResEnum.BIKE_TYPE_WINDOW, 
				BikeTypeRowDto.getBikeTypeId(), 
				new WindowEventCallback<BikeType>(){
					@Override
					public void onClose(BikeType returnData) {
						loader.load(); //保存完毕后刷新，可能有过滤条件，因此采用刷新方式
					}
				}
		));
	}

	@Override
	protected void doDeleteClick(SelectEvent e) {
		ClientManager.getBikeTypeClient().delete(grid.getSelectionModel().getSelectedItem().getBikeTypeId(), new RestCallback<Void>(){
			@Override
			public void onSuccess(Method method, Void response) {
				Info.display("操作完成", "用户删除成功");
				loader.load(); //保存完毕后刷新，可能有过滤条件，因此采用刷新方式
			}
		});
	}

	@Override
	protected ModelKeyProvider<BikeTypeRowDto> getModelKeyProvider() {
		return new ModelKeyProvider<BikeTypeRowDto>(){
			@Override
			public String getKey(BikeTypeRowDto item) {
				return item.getBikeTypeId().toString();
			}
		};
	}
	
	interface BikeTypeProperties extends PropertyAccess<BikeTypeRowDto> {
		
		ValueProvider<BikeTypeRowDto, Integer> bikeTypeId();
		ValueProvider<BikeTypeRowDto, String> bikeTypeNm();
		ValueProvider<BikeTypeRowDto, String> orgNm();
		ValueProvider<BikeTypeRowDto, Double> halfhourAmmount();
	}
	
	@Override
	protected void initColumnConfig(List<ColumnConfig<BikeTypeRowDto, ?>> configList) {
		BikeTypeProperties props = GWT.create(BikeTypeProperties.class);
		
		configList.add(new ColumnConfigEx<BikeTypeRowDto, String>(props.bikeTypeNm(), 100, "名称", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<BikeTypeRowDto, String>(props.orgNm(), 100, "运营商", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<BikeTypeRowDto, Double>(props.halfhourAmmount(), 126, "半小时金额（元）", HasHorizontalAlignment.ALIGN_CENTER, true));
	}

	@Override
	protected CallBack<BikeTypeRowDto> getRestReq() {
		return new CallBack<BikeTypeRowDto>(){
			@Override
			public void call(int offset, int limit,final RestyGwtPagingLoader<BikeTypeRowDto> loader) {
				BikeTypeListReq bikeTypeListReq = new BikeTypeListReq();
				if (Global.PLATFORM_ORG_ID != MainFrame.adminUser.getOrgId()) {
					bikeTypeListReq.setOrgId(MainFrame.adminUser.getOrgId());
				} else {
					bikeTypeListReq.setOrgId(orgCombobox.getValue());
				}
				ClientManager.getBikeTypeClient().findBikeTypeList(bikeTypeListReq, offset, limit, new RestCallback<ClientPage<BikeTypeRowDto>>(){
					@Override
					public void onSuccess(Method method, ClientPage<BikeTypeRowDto> response) {
						loader.onLoad(response.getList(), response.getTotalLength(), response.getOffset());
					}
				});
			}
		};
	}

	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		return widget;
	}

	@Override
	public void onModuleReload(BodyContentEvent event) {
		super.onModuleReload(event);
		// 运营商登陆
		if (Global.PLATFORM_ORG_ID != MainFrame.adminUser.getOrgId()) {
			orgCombobox.hide();
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
	}
}
