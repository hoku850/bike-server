package org.ccframe.client.module.core.view;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.base.BaseWindowView;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.ViewUtil;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

@Singleton
public class PreferenceTextEditWindowView extends BaseWindowView<String, Void>{

	interface PreferenceTextEditUiBinder extends UiBinder<Component, PreferenceTextEditWindowView> {}
	
	private static PreferenceTextEditUiBinder uiBinder = GWT.create(PreferenceTextEditUiBinder.class);

	private String paramInnerCoding;
	
	@UiField(provided = true)
	ListView<RowText, String> textList;

	ListStore<RowText> listStore;

	@UiField
	public TextButton addButton;

	@UiField
	public TextButton editButton;

	@UiField
	public TextButton deleteButton;

	private static final class RowText{
		private Integer index;
		private String text;
		
		public RowText(){}
		public RowText(Integer index, String text){
			this.index = index;
			this.text = text;
		}
		public Integer getIndex() {
			return index;
		}
		public void setIndex(Integer index) {
			this.index = index;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		
	}
	
	@Override
	protected Widget bindUi() {
		listStore = new ListStore<RowText>(new ModelKeyProvider<RowText>(){
			@Override
			public String getKey(RowText item) {
				return item.getIndex().toString();
			}
		});
		textList = new ListView<RowText, String>(listStore, new ValueProvider<RowText, String>(){
			@Override
			public String getValue(RowText rowText) {
				return rowText.getText();
			}
			@Override
			public void setValue(RowText rowText, String value) {
			}
			@Override
			public String getPath() {
				return null;
			}
		});
		Widget widget = uiBinder.createAndBindUi(this);
		window.addHideHandler(new HideHandler(){
			@Override
			public void onHide(HideEvent event) {
				PreferenceTextEditWindowView.this.retCallBack.onClose(null);
			}
		});
		textList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		return widget;
	}

	@Override
	protected void onLoadData(String paramInnerCoding) {
		this.paramInnerCoding = paramInnerCoding;
		ClientManager.getParamClient().getPreferenceText(this.paramInnerCoding, new RestCallback<List<String>>(){
			@Override
			public void onSuccess(Method method, List<String> response) {
				listStore.clear();
				for(int i = 0; i < response.size(); i ++){
					listStore.add(new RowText(i, response.get(i)));
				}
				textList.getSelectionModel().select(0, false); //默认选中第一条
			}
		});
	}

	private void sendAllText(){
		List<String> preferenceTextList = new ArrayList<String>();
		for(RowText rowText:listStore.getAll()){
			preferenceTextList.add(rowText.getText());
		}
		ClientManager.getParamClient().setPreferenceText(preferenceTextList, paramInnerCoding, new RestCallback<Void>(){
			@Override
			public void onSuccess(Method method, Void response) {
				if(textList.getSelectionModel().getSelectedItem() == null){
					textList.getSelectionModel().select(0, false);
				}
			}
		});
	}
	
	@UiHandler({"addButton","editButton"})
	public void handleAddClick(SelectEvent e){
		final boolean isAdd = (e.getSource() == addButton);
		final RowText selectItem = textList.getSelectionModel().getSelectedItem();
		ViewUtil.prompt(isAdd ? "新增": "修改" + "预设文本", "文本内容", isAdd ? "" : selectItem.getText(), new ViewUtil.PromptCallback() {
			@Override
			public void onOK(String inputText) {
				if(isAdd){
					listStore.add(new RowText(listStore.size(), inputText));
				}else{
					selectItem.setText(inputText);
					listStore.update(selectItem);
				}
				sendAllText();
			}
		});
	}

	@UiHandler("deleteButton")
	public void handleDeleteClick(SelectEvent e){
		if(textList.getStore().size() <= 1){
			ViewUtil.error("系统信息", "至少需要保留一条预设文本");
			return;
		}
		ViewUtil.confirm("系统信息", "您确定要删除选择的预设文本吗？删除后将不可恢复", new Runnable(){
			@Override
			public void run() {
				listStore.remove(textList.getSelectionModel().getSelectedItem());
				sendAllText();
			}
		});
	}

}

