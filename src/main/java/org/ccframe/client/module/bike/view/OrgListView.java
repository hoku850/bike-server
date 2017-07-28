package org.ccframe.client.module.bike.view;

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
import org.ccframe.client.commons.WindowEventCallback;
import org.ccframe.client.module.core.event.LoadWindowEvent;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.dto.OrgListReq;
import org.ccframe.subsys.core.dto.OrgRowDto;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

@Singleton
public class OrgListView extends BaseCrudListView<OrgRowDto>{

	interface OrgListUiBinder extends UiBinder<Component, OrgListView> {}
	private static OrgListUiBinder uiBinder = GWT.create(OrgListUiBinder.class);
	
	@Override
	public void handleAddClick(SelectEvent e) {
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, Org, EventHandler>(ViewResEnum.AGENT_WINDOW, null, new WindowEventCallback<Org>(){
			@Override
			public void onClose(Org returnData) {
				loader.load(); //保存完毕后刷新
			}
		}));
	}
	
	@Override
	protected void onEditClick(SelectEvent e) {
		OrgRowDto orgRowDto = grid.getSelectionModel().getSelectedItem();
		
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, Org, EventHandler>(
			ViewResEnum.AGENT_WINDOW, 
			orgRowDto.getOrgId(), 
			new WindowEventCallback<Org>(){
				@Override
				public void onClose(Org returnData) {
					loader.load(); //保存完毕后刷新，可能有过滤条件，因此采用刷新方式
				}
			}
		));
	}
	
	@Override
	protected void doDeleteClick(SelectEvent e) {
		
	}
	
	@Override
	protected ModelKeyProvider<OrgRowDto> getModelKeyProvider() {
		return new ModelKeyProvider<OrgRowDto>(){
			@Override
			public String getKey(OrgRowDto item) {
				return item.getOrgId().toString();
			}
		};
	}
	
	interface AgentProperties extends PropertyAccess<OrgRowDto> {
		
		ValueProvider<OrgRowDto, Integer> orgId();
		ValueProvider<OrgRowDto, String> orgNm();
		ValueProvider<OrgRowDto, String> manager();
		ValueProvider<OrgRowDto, String> managerTel();
		ValueProvider<OrgRowDto, Double> chargeTotalValue();
		ValueProvider<OrgRowDto, Long> cyclingNum();
		ValueProvider<OrgRowDto, Double> cyclingIncome();
	}
	
	
	@Override
	protected void initColumnConfig(List<ColumnConfig<OrgRowDto, ?>> configList) {
		AgentProperties props = GWT.create(AgentProperties.class);
		
		configList.add(new ColumnConfigEx<OrgRowDto, Integer>(props.orgId(), 100, "编号", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<OrgRowDto, String>(props.orgNm(), 100, "运营商名称", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<OrgRowDto, String>(props.manager(), 100, "联系人", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<OrgRowDto, String>(props.managerTel(), 120, "联系电话", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<OrgRowDto, Double>(props.chargeTotalValue(), 100, "充值总金额", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<OrgRowDto, Long>(props.cyclingNum(), 100, "骑行订单数", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<OrgRowDto, Double>(props.cyclingIncome(), 100, "骑行总收入", HasHorizontalAlignment.ALIGN_CENTER, false));
	}
	
	@Override
	protected CallBack<OrgRowDto> getRestReq() {
		return new CallBack<OrgRowDto>(){
			@Override
			public void call(int offset, int limit,final RestyGwtPagingLoader<OrgRowDto> loader) {
				OrgListReq agentListReq = new OrgListReq();
				ClientManager.getOrgClient().findOrgList(agentListReq, offset, limit, new RestCallback<ClientPage<OrgRowDto>>(){
					@Override
					public void onSuccess(Method method, ClientPage<OrgRowDto> response) {
						loader.onLoad(response.getList(), response.getTotalLength(), response.getOffset());
					}
				});
			}
		};
	}
	
	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		deleteButton.hide();
		return widget;
	}
}
