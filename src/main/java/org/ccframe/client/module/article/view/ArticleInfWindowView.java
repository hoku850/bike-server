package org.ccframe.client.module.article.view;

import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.CcFormPanelHelper;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.TreeRootEnum;
import org.ccframe.client.components.CcCkEditor;
import org.ccframe.client.components.CcEnumRadioField;
import org.ccframe.client.components.CcTextField;
import org.ccframe.client.components.CcTreePathField;
import org.ccframe.client.components.CcVBoxLayoutContainer;
import org.ccframe.client.module.article.vo.ArticleInfWindowReq;
import org.ccframe.subsys.article.dto.ArticleInfDto;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.info.Info;

@Singleton
public class ArticleInfWindowView extends BaseWindowView<ArticleInfWindowReq, ArticleInfDto> implements Editor<ArticleInfDto>{

	interface ArticleInfUiBinder extends UiBinder<Component, ArticleInfWindowView> {}
	interface ArticleInfDriver extends SimpleBeanEditorDriver<ArticleInfDto, ArticleInfWindowView> { }
	
	private static ArticleInfUiBinder uiBinder = GWT.create(ArticleInfUiBinder.class);

	private Integer articleInfId;
	
	@UiField
	CcVBoxLayoutContainer vBoxLayoutContainer;
	
	@UiField
	CcTextField articleTitle;
	
	@UiField
	CcTextField articleAuthor;
	
	@UiField
	CcTextField articleSource;

	@UiField
	CcEnumRadioField approveStatCode;

	@UiField
	IntegerField articlePosition;

	@UiField
	CcTreePathField articleCategoryId;
	
	@UiField
	CcCkEditor articleContStr;

	@UiHandler("saveButton")
	public void handleSaveClick(SelectEvent e){
		if(FormPanelHelper.isValid(vBoxLayoutContainer, false)){
			final ArticleInfDto articleInfDto = driver.flush();
			articleInfDto.setArticleInfId(articleInfId);
			articleInfDto.setFileInfBarDtoList(articleContStr.getFileInfBarDtoList());
			final TextButton button = ((TextButton)(e.getSource()));
			button.disable();
			
			ClientManager.getArticleInfClient().saveOrUpdateArticleInfDto(articleInfDto, new RestCallback<Void>(){
				@Override
				public void onSuccess(Method method, Void response) {
					Info.display("操作完成", "文章" + (articleInfId == null ? "新增" : "修改") + "成功");
					ArticleInfWindowView.this.retCallBack.onClose(articleInfDto); //保存并回传结果数据
					button.enable();
					window.hide();
				}
				@Override
				protected void afterFailure(){ //如果采用按钮的disable逻辑，一定要在此方法enable按钮
					button.enable();
				}
			});
		}
	}
	
	private ArticleInfDriver driver = GWT.create(ArticleInfDriver.class);

	@Override
	protected Widget bindUi() {
		Widget widget = uiBinder.createAndBindUi(this);
		driver.initialize(this);

		driver.edit(new ArticleInfDto());

		return widget;
	}

	@Override
	protected void onLoadData(ArticleInfWindowReq reqType) {
//		CcFormPanelHelper.clearInvalid(vBoxLayoutContainer);
		CcFormPanelHelper.reset(vBoxLayoutContainer);
		if(reqType.getArticleInfId() == null){
			if(reqType.getArticleCategoryId() == null){ //根分类
				articleCategoryId.setValue(TreeRootEnum.ARTICLE_CATEGORY_TREE_ROOT.getTreeNodeId());
			}else{
				articleCategoryId.setValue(reqType.getArticleCategoryId());
			}
			articleInfId = null;
//			driver.edit(new ArticleInfDto());
			articleContStr.clear();
			window.setHeadingText("新增文章");
		}else{
			articleInfId = reqType.getArticleInfId();
			ClientManager.getArticleInfClient().getById(articleInfId, new RestCallback<ArticleInfDto>(){
				@Override
				public void onSuccess(Method method, ArticleInfDto response) {
					driver.edit(response);
					articleContStr.setFileInfBarDtoList(response.getFileInfBarDtoList());
				}
			});
			window.setHeadingText("修改文章");
			
		}
	}


}

