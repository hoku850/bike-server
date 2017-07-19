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
import org.ccframe.client.commons.WindowEventCallback;
import org.ccframe.client.commons.RestyGwtPagingLoader.CallBack;
import org.ccframe.client.module.core.event.BodyContentEvent;
import org.ccframe.client.module.core.event.LoadWindowEvent;
import org.ccframe.subsys.bike.domain.entity.AgentApp;
import org.ccframe.subsys.bike.dto.AgentAppListReq;
import org.ccframe.subsys.bike.dto.AgentAppRowDto;
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
import com.sencha.gxt.widget.core.client.info.Info;

@Singleton
public class AgentAppListView extends BaseCrudListView<AgentAppRowDto>{

	interface AgentAppListUiBinder extends UiBinder<Component, AgentAppListView> {}
	private static AgentAppListUiBinder uiBinder = GWT.create(AgentAppListUiBinder.class);
	
	
	
	@Override
	public void handleAddClick(SelectEvent e) {
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, AgentApp, EventHandler>(ViewResEnum.AGENT_APP_WINDOW, null, new WindowEventCallback<AgentApp>(){
			@Override
			public void onClose(AgentApp returnData) {
				loader.load(); //保存完毕后刷新
			}
		}));
	}
	
	@Override
	protected void onEditClick(SelectEvent e) {
		AgentAppRowDto agentAppRowDto = grid.getSelectionModel().getSelectedItem();
		
		EventBusUtil.fireEvent(new LoadWindowEvent<Integer, AgentApp, EventHandler>(
			ViewResEnum.AGENT_APP_WINDOW, 
			agentAppRowDto.getAgentAppId(), 
			new WindowEventCallback<AgentApp>(){
				@Override
				public void onClose(AgentApp returnData) {
					loader.load(); //保存完毕后刷新，可能有过滤条件，因此采用刷新方式
				}
			}
		));
	}
	
	@Override
	protected void doDeleteClick(SelectEvent e) {
		ClientManager.getAgentAppClient().delete(grid.getSelectionModel().getSelectedItem().getAgentAppId(), new RestCallback<Void>(){
			@Override
			public void onSuccess(Method method, Void response) {
				Info.display("操作完成", "运营商APP删除成功");
				loader.load(); //保存完毕后刷新，可能有过滤条件，因此采用刷新方式
			}
		});
	}
	
	@Override
	protected ModelKeyProvider<AgentAppRowDto> getModelKeyProvider() {
		return new ModelKeyProvider<AgentAppRowDto>(){
			@Override
			public String getKey(AgentAppRowDto item) {
				return item.getAgentAppId().toString();
			}
		};
	}
	
	interface AgentAppProperties extends PropertyAccess<AgentAppRowDto> {
		
		ValueProvider<AgentAppRowDto, String> orgNm();
		ValueProvider<AgentAppRowDto, String> appNm();
		ValueProvider<AgentAppRowDto, String> isoUrl();
		ValueProvider<AgentAppRowDto, String> androidUrl();
	}
	
	
	@Override
	protected void initColumnConfig(List<ColumnConfig<AgentAppRowDto, ?>> configList) {
		AgentAppProperties props = GWT.create(AgentAppProperties.class);
		
		configList.add(new ColumnConfigEx<AgentAppRowDto, String>(props.orgNm(), 70, "运营商", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<AgentAppRowDto, String>(props.appNm(), 70, "APP名称", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<AgentAppRowDto, String>(props.androidUrl(), 100, "IOS URL", HasHorizontalAlignment.ALIGN_CENTER, false));
		configList.add(new ColumnConfigEx<AgentAppRowDto, String>(props.isoUrl(), 100, "安卓 URL", HasHorizontalAlignment.ALIGN_CENTER, false));
	}
	
	@Override
	protected CallBack<AgentAppRowDto> getRestReq() {
		return new CallBack<AgentAppRowDto>(){
			@Override
			public void call(int offset, int limit,final RestyGwtPagingLoader<AgentAppRowDto> loader) {
				AgentAppListReq agentAppListReq = new AgentAppListReq();
				
				ClientManager.getAgentAppClient().findAgentAppList(agentAppListReq, offset, limit, new RestCallback<ClientPage<AgentAppRowDto>>(){
					@Override
					public void onSuccess(Method method, ClientPage<AgentAppRowDto> response) {
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
	}
}
