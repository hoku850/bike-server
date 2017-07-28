package org.ccframe.client.module.article.view;

import java.util.List;

import org.ccframe.client.ViewResEnum;
import org.ccframe.client.base.BaseCrudListView;
import org.ccframe.client.base.BaseHandler;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.client.commons.ColumnConfigEx;
import org.ccframe.client.commons.EventBusUtil;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.RestyGwtPagingLoader;
import org.ccframe.client.commons.RestyGwtPagingLoader.CallBack;
import org.ccframe.client.commons.TreeNodeTree;
import org.ccframe.client.commons.WindowEventCallback;
import org.ccframe.client.components.CcTextField;
import org.ccframe.client.module.article.event.ArticleCategorySelectEvent;
import org.ccframe.client.module.article.vo.ArticleInfWindowReq;
import org.ccframe.client.module.core.event.BodyContentEvent;
import org.ccframe.client.module.core.event.LoadWindowEvent;
import org.ccframe.subsys.article.domain.entity.ArticleInf;
import org.ccframe.subsys.article.dto.ArticleInfListReq;
import org.ccframe.subsys.article.dto.ArticleInfRowDto;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.shared.EventHandler;
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
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

@Singleton
public class ArticleInfListView extends BaseCrudListView<ArticleInfRowDto>{

	interface ArticleInfListUiBinder extends UiBinder<Component, ArticleInfListView> {}
	private static ArticleInfListUiBinder uiBinder = GWT.create(ArticleInfListUiBinder.class);
	
	private TreeNodeTree selectionNode;

	@UiField
	CcTextField articleSearch;
	

	@UiHandler("articleSearch")
	public void handleTextEnter(KeyPressEvent e){
		if(e.getNativeEvent().getKeyCode() == 13){ //回车搜索
			loader.load();
		}
	}
	
	@UiHandler("searchButton")
	public void handlesearchButtonClick(SelectEvent e){
		loader.load();
	}

	@Override
	protected ModelKeyProvider<ArticleInfRowDto> getModelKeyProvider() {
		return new ModelKeyProvider<ArticleInfRowDto>(){
			@Override
			public String getKey(ArticleInfRowDto item) {
				return item.getArticleInfId().toString();
			}
		};
	}

	interface ArticleInfProperties extends PropertyAccess<ArticleInfRowDto> {
		ValueProvider<ArticleInfRowDto, Integer> articlePosition();
		ValueProvider<ArticleInfRowDto, Integer> articleInfId();
		ValueProvider<ArticleInfRowDto, String> articleTitle();
		ValueProvider<ArticleInfRowDto, String> articleCategoryPath();
		ValueProvider<ArticleInfRowDto, Integer> browseTimes();
		ValueProvider<ArticleInfRowDto, String> releaseTimeStr();
	}
	
	@Override
	protected void initColumnConfig(List<ColumnConfig<ArticleInfRowDto, ?>> configList) {
		ArticleInfProperties props = GWT.create(ArticleInfProperties.class);
		configList.add(new ColumnConfigEx<ArticleInfRowDto, Integer>(props.articlePosition(), 50, "排序", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<ArticleInfRowDto, Integer>(props.articleInfId(), 62, "文章ID", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<ArticleInfRowDto, String>(props.articleTitle(), 150, "标题", HasHorizontalAlignment.ALIGN_LEFT, false));
		configList.add(new ColumnConfigEx<ArticleInfRowDto, String>(props.articleCategoryPath(), 100, "文章分类", HasHorizontalAlignment.ALIGN_LEFT, false));
		configList.add(new ColumnConfigEx<ArticleInfRowDto, Integer>(props.browseTimes(), 75, "访问次数", HasHorizontalAlignment.ALIGN_CENTER, true));
		configList.add(new ColumnConfigEx<ArticleInfRowDto, String>(props.releaseTimeStr(), 140, "发布时间", HasHorizontalAlignment.ALIGN_CENTER, true));
	}

	@Override
	public void handleAddClick(SelectEvent e) {
		EventBusUtil.fireEvent(new LoadWindowEvent<ArticleInfWindowReq, ArticleInf, EventHandler>(ViewResEnum.ARTICLE_INF_WINDOW, new ArticleInfWindowReq(null, selectionNode == null ? null: selectionNode.getTreeNodeId()), new WindowEventCallback<ArticleInf>(){
			@Override
			public void onClose(ArticleInf returnData) {
				loader.load(); //保存完毕后刷新
			}
		}));
	}


	@Override
	protected void onEditClick(SelectEvent e) {
		ArticleInfRowDto selectedRow = grid.getSelectionModel().getSelectedItem();
		
		EventBusUtil.fireEvent(new LoadWindowEvent<ArticleInfWindowReq, ArticleInf, EventHandler>(
				ViewResEnum.ARTICLE_INF_WINDOW,
				new ArticleInfWindowReq(selectedRow.getArticleInfId(), null), 
				new WindowEventCallback<ArticleInf>(){
			@Override
			public void onClose(ArticleInf returnData) {
				loader.load(); //保存完毕后刷新，可能有过滤条件，因此采用刷新方式
			}
		}));
	}

	@Override
	protected void doDeleteClick(SelectEvent e) {
		ClientManager.getArticleInfClient().delete(grid.getSelectionModel().getSelectedItem().getArticleInfId(), new RestCallback<Void>(){
			@Override
			public void onSuccess(Method method, Void response) {
				loader.load(); //保存完毕后刷新，可能有过滤条件，因此采用刷新方式
			}
		});
	}


	
	/**
	 * 由于需要等待树选择消息再加载，因此在初始化时不需要加载.
	 */
	@Override 
	public void onModuleReload(BodyContentEvent event) {}
	
	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		EventBusUtil.addHandler(ArticleCategorySelectEvent.TYPE, new BaseHandler<ArticleCategorySelectEvent>(){
			@Override
			public void action(ArticleCategorySelectEvent event) {
				selectionNode = event.getObject();
				loader.load();
			}
		});
		return widget;
	}

	@Override
	protected CallBack<ArticleInfRowDto> getRestReq() {
		
		return new CallBack<ArticleInfRowDto>(){
			@Override
			public void call(int offset, int limit,final RestyGwtPagingLoader<ArticleInfRowDto> loader) {
				ArticleInfListReq articleInfListReq = new ArticleInfListReq();
				if(selectionNode != null){
					articleInfListReq.setArticleCategoryId(selectionNode.getTreeNodeId());
				}
				articleInfListReq.setArticleSearch(articleSearch.getValue());

				ClientManager.getArticleInfClient().findArticleList(articleInfListReq, offset / limit, limit, new RestCallback<ClientPage<ArticleInfRowDto>>(){
					@Override
					public void onSuccess(Method method, ClientPage<ArticleInfRowDto> response) {
						loader.onLoad(response.getList(), response.getTotalLength(), response.getOffset());
					}
				});
			}
		};
	}

}
