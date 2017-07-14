package org.ccframe.client.components;

import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.RestCallback;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.AdapterField;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * 根据后台LabelValue提供器获得下拉列表
 * 当searchAllText不为null时，添加一个searchAllText的项到第一个，选择的结果值是0（对于数据库对象ID，0为保留值）
 * defaultValue必须设置，如果要默认为全部，则需设置为0
 * @author JIM
 *
 */
public class CcLabelValueCombobox extends AdapterField<Integer>{

	private Integer defaultValue;
	private ListStore<LabelValue> store;	
	private ComboBox<LabelValue> combobox;
	protected Integer value;
	private Integer pendingValue;
	private String searchAllText;
	private String extraParam; //获取服务器列表时可以传递额外的参数来进行查询，通常用于级联
	private boolean reloading;
	/**
	 * 判断是否在请求中的状态，如果有则将数据保存到pendingValue
	 */
	private String beanName;

	private static LabelProvider<LabelValue> provider = new LabelProvider<LabelValue>(){
		@Override
		public String getLabel(LabelValue item) {
			return item.getLabel();
		}
	};
	
	public String getExtraParam() {
		return extraParam;
	}

	public void setExtraParam(String extraParam) {
		this.extraParam = extraParam;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
	public CcLabelValueCombobox() {
		this(new ComboBox<LabelValue>(new ListStore<LabelValue>(new ModelKeyProvider<LabelValue>(){
			@Override
			public String getKey(LabelValue item) {
				return item.getValue().toString();
			}
		}), provider));
	}

	public CcLabelValueCombobox(Widget widget) {
		super(widget);
		combobox = (ComboBox<LabelValue>)this.getWidget();
		combobox.setForceSelection(true);
		combobox.setTriggerAction(TriggerAction.ALL);
		combobox.setEditable(false);
		combobox.addSelectionHandler(new SelectionHandler<LabelValue>(){
			@Override
			public void onSelection(SelectionEvent<LabelValue> event) {
				setValue(event.getSelectedItem().getValue());
			}
		});
		combobox.addSelectionHandler(new SelectionHandler<LabelValue>(){
			@Override
			public void onSelection(SelectionEvent<LabelValue> event) {
				setValue(event.getSelectedItem().getValue());
			}
		});
		combobox.addStyleName("ccCombo");
		store = combobox.getStore();
	}


	public void setDefaultValue(Integer defaultValue){
		this.defaultValue = defaultValue;
	}

	@Override
	public Integer getValue() {
		return value;
	}

	@Override
	public void setValue(Integer value) {
		if(reloading){
			pendingValue = value;
		}else{
			if(pendingValue != null){
				value = pendingValue;
				pendingValue = null;
			}
			innerSetValue(value);
		}
	}

	private void innerSetValue(Integer value){
		Integer oldValue = this.value;
		if (value != oldValue && (oldValue == null || !oldValue.equals(value))) {
			this.value = value;
			combobox.setValue(store.findModelWithKey(value.toString()));
			ValueChangeEvent.fire(this, value);
		}
	}

	public void setSearchAllText(String searchAllText){
		this.searchAllText = searchAllText;
	}
	
	@Override
	public void reset() {
		if(reloading){
			return;
		}
		reloading = true;
		this.value = null;
		if(defaultValue != null){
			setValue(defaultValue);
		}
		ClientManager.getSimpleLabelValueClient().getLabelValueList(beanName, extraParam, new RestCallback<List<LabelValue>>(){
			@Override
			public void onSuccess(Method method, List<LabelValue> response) {
				//清理原radio
				store.clear();
				//载入新radio
				if(searchAllText != null){ //添加按钮“全部”
					store.add(new LabelValue("<" + searchAllText + ">", Global.COMBOBOX_ALL_VALUE));
				}
				for(LabelValue labelValue: response){
					store.add(labelValue);
				}
				reloading = false;
				if(defaultValue != null){
					setValue(pendingValue);
				}else{ //没设置defaultValue就默认第一个
					if(response.size() > 0){
						setValue(response.get(0).getValue());
					}
				}
			}
			@Override
			protected void afterFailure() {
				reloading = false;
			}
		});
	}

	public void setReadOnly(boolean readonly) {
		combobox.setReadOnly(readonly);
		if(readonly == false){
			combobox.setEditable(false);
		}
	}


}
