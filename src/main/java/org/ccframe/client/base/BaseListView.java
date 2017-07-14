package org.ccframe.client.base;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ICcModule;
import org.ccframe.client.commons.RestyGwtPagingLoader;
import org.ccframe.client.commons.RestyGwtPagingLoader.CallBack;
import org.ccframe.client.module.core.event.BodyContentEvent;

import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;

public abstract class BaseListView<T> implements ICcModule{

	public Widget widget; 
	
	@UiField(provided = true)
	public ColumnModel<T> columnModel;

	@UiField(provided = true)
	public ListStore<T> store;

	@UiField(provided = true)
	public RestyGwtPagingLoader<T> loader;
	
	@UiField
	public Grid<T> grid;
	
	@UiField
	public GridView<T> view;
	
	protected abstract ModelKeyProvider<T> getModelKeyProvider();

	abstract protected void initColumnConfig(List<ColumnConfig<T, ?>> configList);

	abstract protected void bindOther();
	
	@Override
	public void onModuleReload(BodyContentEvent event) {
		loader.load();
	}

	@Override
	public Widget asWidget() {
		if(widget == null){
			List<ColumnConfig<T, ?>> configList = new ArrayList<ColumnConfig<T, ?>>();
			initColumnConfig(configList);

			store = new ListStore<T>(getModelKeyProvider());
			columnModel = new ColumnModel<T>(configList);
			loader = new RestyGwtPagingLoader<T>();
			loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, T, PagingLoadResult<T>>(store)); //将loader的数据存储到store
			loader.setCallBack(getRestReq()); //绑定loader的数据请求方式
			widget = bindUi();

			grid.setLoadMask(true);
			grid.setAllowTextSelection(true);
			grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

			bindOther();
			view.setForceFit(true);
		}
		return widget;
	}

	/**
	 * restyGwt请求
	 * @return
	 */
	protected abstract CallBack<T> getRestReq();

	/**
	 * 界面初始化工作.
	 * @return
	 */
	abstract protected Widget bindUi();


}
