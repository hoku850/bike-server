package org.ccframe.client.module.core.view;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.base.BaseListView;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.ColumnConfigEx;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.RestyGwtPagingLoader;
import org.ccframe.client.commons.RestyGwtPagingLoader.CallBack;
import org.ccframe.client.commons.StringUtils;
import org.ccframe.subsys.core.dto.CacheInf;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
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
public class CacheInfListView extends BaseListView<CacheInf>{

	interface CacheInfListUiBinder extends UiBinder<Component, CacheInfListView> {}
	private static CacheInfListUiBinder uiBinder = GWT.create(CacheInfListUiBinder.class);
	
	private NumberFormat hitPercentFormat = NumberFormat.getFormat("00.00");

	@UiHandler("clearAllButton")
	public void clearAllButtonClick(SelectEvent e){
		CacheInf selectedItem = grid.getSelectionModel().getSelectedItem();
		final List<String> clearList = new ArrayList<String>(selectedItem.getEvictLinkCacheList());
		clearList.add(selectedItem.getCacheName());
		ClientManager.getCacheInfClient().clearCaches(StringUtils.join(clearList, Global.ENUM_TEXT_SPLIT_CHAR), new RestCallback<Void>(){
				public void onSuccess(Method method, Void response) {
				Info.display("操作完成", "缓存清理成功");
				loader.load();
			}
		});
	}
	
	@Override
	protected ModelKeyProvider<CacheInf> getModelKeyProvider() {
		return new ModelKeyProvider<CacheInf>(){
			@Override
			public String getKey(CacheInf item) {
				return item.getCacheName().toString();
			}
		};
	}

	interface ParamRowProperties extends PropertyAccess<CacheInf> {
		ValueProvider<CacheInf, String> getCacheName();
		ValueProvider<CacheInf, Double> getHitRatio();
		ValueProvider<CacheInf, Long> getHitCount();
		ValueProvider<CacheInf, Long> getMissCount();

		ValueProvider<CacheInf, Long> getTimeToLiveSeconds();
		ValueProvider<CacheInf, Long> getSize();
		ValueProvider<CacheInf, Long> getMaxSize();
		
		ValueProvider<CacheInf, List<String>> getEvictLinkCacheList();
	}
	
	@Override
	protected void initColumnConfig(List<ColumnConfig<CacheInf, ?>> configList) {
		ParamRowProperties props = GWT.create(ParamRowProperties.class);
		configList.add(new ColumnConfigEx<CacheInf, String>(props.getCacheName(), 100, "缓存名称", HasHorizontalAlignment.ALIGN_CENTER, false));
		
		ColumnConfigEx cacheSizeColumnConfig = new ColumnConfigEx<CacheInf, String>(props.getCacheName(), 110, "缓存数量/容量", HasHorizontalAlignment.ALIGN_CENTER, true);
		cacheSizeColumnConfig.setCell(new AbstractCell<String>(){
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
				CacheInf cacheInf = store.get(context.getIndex());
				sb.appendHtmlConstant(cacheInf.getSize() + "/" + cacheInf.getMaxSize());
			}
		});
		configList.add(cacheSizeColumnConfig);

		configList.add(new ColumnConfigEx<CacheInf, Long>(props.getTimeToLiveSeconds(), 110, "缓存时长(秒)", HasHorizontalAlignment.ALIGN_CENTER, true));
		
		ColumnConfigEx cacheHitColumnConfig = new ColumnConfigEx<CacheInf, String>(props.getCacheName(), 130, "命中次数/总次数", HasHorizontalAlignment.ALIGN_CENTER, true); 
		cacheHitColumnConfig.setCell(new AbstractCell<String>(){
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
				CacheInf cacheInf = store.get(context.getIndex());
				sb.appendHtmlConstant(cacheInf.getHitCount() + "/" + (cacheInf.getHitCount() + cacheInf.getMissCount()));
			}
		});
		configList.add(cacheHitColumnConfig);

		ColumnConfigEx cacheHitPercentColumnConfig = new ColumnConfigEx<CacheInf, String>(props.getCacheName(), 80, "命中率", HasHorizontalAlignment.ALIGN_CENTER, true); 
		cacheHitPercentColumnConfig.setCell(new AbstractCell<String>(){
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
				CacheInf cacheInf = store.get(context.getIndex());
				if(cacheInf.getHitCount() == 0){
					sb.appendHtmlConstant("0.00%");
				}else{
					sb.appendHtmlConstant(hitPercentFormat.format( (double)cacheInf.getHitCount() * 100d / (double)(cacheInf.getHitCount() + cacheInf.getMissCount())) + "%");
				}
			}
		});
		configList.add(cacheHitPercentColumnConfig);

		ColumnConfigEx referCacheColumnConfig = new ColumnConfigEx<CacheInf, String>(props.getCacheName(), 100, "关联缓存", HasHorizontalAlignment.ALIGN_CENTER, false); 
		referCacheColumnConfig.setCell(new AbstractCell<String>(){
			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context, String value, SafeHtmlBuilder sb) {
				CacheInf cacheInf = store.get(context.getIndex());
				sb.appendHtmlConstant(StringUtils.join(cacheInf.getEvictLinkCacheList(), "<br/>"));
			}
		});
		configList.add(referCacheColumnConfig);
	}

	@Override
	protected Widget bindUi() {
		return uiBinder.createAndBindUi(this);
	}

	@Override
	protected CallBack<CacheInf> getRestReq() {
		return new CallBack<CacheInf>(){
			@Override
			public void call(int offset, int limit,final RestyGwtPagingLoader<CacheInf> loader) {
				ClientManager.getCacheInfClient().findCacheList(new RestCallback<List<CacheInf>>(){
					@Override
					public void onSuccess(Method method, List<CacheInf> response) {
						loader.onLoad(response, response.size(), 0);
						grid.getSelectionModel().select(0, false);
					}
				});
			}
		};
	}

	@Override
	protected void bindOther() {
		
	}

}
