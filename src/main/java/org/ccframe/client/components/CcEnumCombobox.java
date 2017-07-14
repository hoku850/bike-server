package org.ccframe.client.components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ccframe.client.Global;
import org.ccframe.client.commons.ICodeEnum;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.widget.core.client.form.AdapterField;
import com.sencha.gxt.widget.core.client.form.StringComboBox;

/**
 * Enum支持的下拉combobox，可用于搜索.
 * 
 * searchAllText 如果没有设置，则不会显示“全部”的选项。“全部”的选项通常用于搜索。
 * defaultEnum 如果不是做搜索而是添加修改，则需要设置此参数。
 * 
 * @author JIM
 *
 */
public class CcEnumCombobox extends AdapterField<String>{

	private ICodeEnum defaultEnum;
	private List<ICodeEnum> valueList;
	private String enumTexts;
	private Map<String, ICodeEnum> enumMap;
	private StringComboBox combobox;
	protected String value;
	private String searchAllText;
	
	public static final ICodeEnum ENUM_ALL = new ICodeEnum(){
		@Override
		public String toCode() {
			return "";
		}
		@Override
		public List<ICodeEnum> valueList() {
			return null;
		}
	};
	
	public CcEnumCombobox() {
		this(new StringComboBox(){
			@Override
			public void reset() {
			}
		});
	}

	public CcEnumCombobox(Widget widget) {
		super(widget);
		combobox = (StringComboBox)this.getWidget();
		combobox.setForceSelection(true);
		combobox.setTriggerAction(TriggerAction.ALL);
		combobox.setEditable(false);
		combobox.addValueChangeHandler(new ValueChangeHandler<String>(){
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				value = enumMap.get(event.getValue()).toCode();
				ValueChangeEvent.fire(CcEnumCombobox.this, value);
			}
		});
		combobox.addSelectionHandler(new SelectionHandler<String>(){
			@Override
			public void onSelection(SelectionEvent<String> event) {
				value = enumMap.get(event.getSelectedItem()).toCode();
				ValueChangeEvent.fire(CcEnumCombobox.this, value);
			}
		});
		combobox.addStyleName("ccCombo");
	}

	public void setDefault(ICodeEnum codeEnum){
		defaultEnum = codeEnum;
		valueList = codeEnum.valueList();
	}

	/**
	 * 设置文本，只有当设置文本时，才会自动根据文本来初始化各Enum
	 * @param enumTexts
	 */
	public void setEnumTexts(String enumTexts){
		this.enumTexts = enumTexts;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		String oldValue = this.value;
		if (value != oldValue && (oldValue == null || !oldValue.equals(value))) {
			this.value = value;
			for(Entry<String, ICodeEnum> entry: enumMap.entrySet()){
				if(entry.getValue().toCode().equals(value)){
					combobox.setValue(entry.getKey(), false);
					break;
				}
			}
			ValueChangeEvent.fire(this, value);
		}

	}

	public void setSearchAllText(String searchAllText){
		this.searchAllText = searchAllText;
	}
	
	@Override
	protected void onAfterFirstAttach() {
		super.onAfterFirstAttach();
		if(valueList != null && enumTexts != null){
			String[] enumText = enumTexts.split(Global.ENUM_TEXT_SPLIT_CHAR);
			int index = 0;
			enumMap = new HashMap<String, ICodeEnum>();

			if(searchAllText != null){ //添加按钮“全部”
				enumMap.put("<" + searchAllText + ">", ENUM_ALL);
				combobox.add("<" + searchAllText + ">");
				defaultEnum = ENUM_ALL;
			}

			for(ICodeEnum enumValue: valueList){
				String value = index < enumText.length ? enumText[index] : Integer.toString(index);
				enumMap.put(value, enumValue);
				combobox.add(value);
				index ++;
			}
			if(defaultEnum != null){
				setValue(defaultEnum.toCode());
			}
		}
	}

	@Override
	public void reset() {
		if(defaultEnum != null){
			setValue(defaultEnum.toCode());
		}
	}

	public void setReadOnly(boolean readonly) {
		combobox.setReadOnly(readonly);
		if(readonly == false){
			combobox.setEditable(false);
		}
	}

}
