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
import org.ccframe.subsys.bike.domain.entity.Agent;
import org.ccframe.subsys.bike.dto.AgentListReq;
import org.ccframe.subsys.bike.dto.AgentRowDto;
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
public class AgentListView extends BaseCrudListView<AgentRowDto>{

	interface AgentListUiBinder extends UiBinder<Component, AgentListView> {}
	private static AgentListUiBinder uiBinder = GWT.create(AgentListUiBinder.class);
	
	@Override
	public void handleAddClick(SelectEvent e) {
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, Agent, EventHandler>(ViewResEnum.AGENT_WINDOW, null, new WindowEventCallback<Agent>(){
			@Override
			public void onClose(Agent returnData) {
				loader.load(); //保存完毕后刷新
			}
		}));
	}
	
	@Override
	protected void onEditClick(SelectEvent e) {
		AgentRowDto AgentRowDto = grid.getSelectionModel().getSelectedItem();
		
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, Agent, EventHandler>(
			ViewResEnum.AGENT_WINDOW, 
			AgentRowDto.getAgentId(), 
			new WindowEventCallback<Agent>(){
				@Override
				public void onClose(Agent returnData) {
					loader.load(); //保存完毕后刷新，可能有过滤条件，因此采用刷新方式
				}
			}
		));
	}
	
	@Override
	protected void doDeleteClick(SelectEvent e) {
		
	}
	
	@Override
	protected ModelKeyProvider<AgentRowDto> getModelKeyProvider() {
		return new ModelKeyProvider<AgentRowDto>(){
			@Override
			public String getKey(AgentRowDto item) {
				return item.getAgentId().toString();
			}
		};
	}
	
	interface AgentProperties extends PropertyAccess<AgentRowDto> {
		
		ValueProvider<AgentRowDto, Integer> agentId();
		ValueProvider<AgentRowDto, String> agentNm();
		ValueProvider<AgentRowDto, String> manager();
		ValueProvider<AgentRowDto, String> managerTel();
		ValueProvider<AgentRowDto, Double> chargeTotalValue();
		ValueProvider<AgentRowDto, Integer> cyclingNum();
		ValueProvider<AgentRowDto, Double> cyclingIncome();
	}
	
	
	@Override
	protected void initColumnConfig(List<ColumnConfig<AgentRowDto, ?>> configList) {
		AgentProperties props = GWT.create(AgentProperties.class);
		
		configList.add(new ColumnConfigEx<AgentRowDto, Integer>(props.agentId(), 100, "编号", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<AgentRowDto, String>(props.agentNm(), 100, "运营商名称", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<AgentRowDto, String>(props.manager(), 100, "联系人", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<AgentRowDto, String>(props.managerTel(), 120, "联系电话", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<AgentRowDto, Double>(props.chargeTotalValue(), 100, "充值总金额", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<AgentRowDto, Integer>(props.cyclingNum(), 100, "骑行订单数", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<AgentRowDto, Double>(props.cyclingIncome(), 100, "骑行总收入", HasHorizontalAlignment.ALIGN_CENTER, false));
	}
	
	@Override
	protected CallBack<AgentRowDto> getRestReq() {
		return new CallBack<AgentRowDto>(){
			@Override
			public void call(int offset, int limit,final RestyGwtPagingLoader<AgentRowDto> loader) {
				AgentListReq agentListReq = new AgentListReq();
				ClientManager.getAgentClient().findAgentList(agentListReq, offset, limit, new RestCallback<ClientPage<AgentRowDto>>(){
					@Override
					public void onSuccess(Method method, ClientPage<AgentRowDto> response) {
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
