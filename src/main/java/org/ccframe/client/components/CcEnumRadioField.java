package org.ccframe.client.components;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.client.commons.ICodeEnum;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.HasValue;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.container.CssFloatLayoutContainer;
import com.sencha.gxt.widget.core.client.form.AdapterField;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.validator.EmptyValidator;

/**
 * 需要在View asWidget时初始化Enum值.
 * 不允许为空（数据库Enum状态都设置非空）
 * 必须设置DefaultEnum及EnumTexts
 * 
 * @author JIM
 *
 */
public class CcEnumRadioField extends AdapterField<String>{
	
	private ICodeEnum defaultEnum;
	private List<ICodeEnum> valueList;
	private String enumTexts;
	protected String value;
	private CssFloatLayoutContainer panel;
	/**
	 * enum和radio的顺序对照表
	 */
	private List<EnumRadio> enumRadioList = new ArrayList<EnumRadio>();
	private ToggleGroup toggle = new ToggleGroup();
	private boolean resetEmpty;

	private class EnumRadio{
		
		private ICodeEnum codeEnum;
		
		private Radio radio;
		
		public EnumRadio(ICodeEnum codeEnum, Radio radio){
			this.codeEnum = codeEnum;
			this.radio = radio;
		}

		public ICodeEnum getCodeEnum() {
			return codeEnum;
		}

		public Radio getRadio() {
			return radio;
		}

	}
	
	public CcEnumRadioField(){
		super(new CssFloatLayoutContainer());
		panel = (CssFloatLayoutContainer)widget;
		panel.getElement().getStyle().setPaddingTop(4, Unit.PX);

		toggle.addValueChangeHandler(new ValueChangeHandler<HasValue<Boolean>>(){
			@Override
			public void onValueChange(ValueChangeEvent<HasValue<Boolean>> event) {
				for(EnumRadio enumRadio: enumRadioList){
					if(enumRadio.getRadio().equals(event.getValue())){
						CcEnumRadioField.this.value = enumRadio.getCodeEnum().toCode();
						break;
					}
				}
				ValueChangeEvent.fire(CcEnumRadioField.this, getValue());
			}
		});
		this.addValidator(new EmptyValidator<String>());
	}

	public void addEnum(ICodeEnum codeEnum, String bindText){
		Radio radio = new Radio(){
			@Override
			public void reset() {
				//do nothing
			}
		};
		radio.setBoxLabel(bindText);
		radio.getElement().getStyle().setPaddingTop(2, Unit.PX);
		radio.getElement().getStyle().setPaddingBottom(2, Unit.PX);
		panel.add(radio);
		toggle.add(radio);
		enumRadioList.add(new EnumRadio(codeEnum, radio));
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
	protected void onAfterFirstAttach() {
		super.onAfterFirstAttach();
		if(valueList != null && enumTexts != null){
			String[] enumText = enumTexts.split(Global.ENUM_TEXT_SPLIT_CHAR);
			int index = 0;
			for(ICodeEnum enumValue: valueList){
				addEnum(enumValue, index < enumText.length ? enumText[index] : "");
				index ++;
			}
		}
	}

	public void setResetEmpty(boolean resetEmpty){
		this.resetEmpty = resetEmpty;
	}
	
	/**
	 * 返回null时表示未选择
	 */
	@Override
	public String getValue() {
		for(EnumRadio enumRadio: enumRadioList){
			if(enumRadio.getRadio().getValue()){
				return enumRadio.getCodeEnum().toCode();
			}
		}
		return defaultEnum == null ? null : defaultEnum.toCode();
	}

	@Override
	public void setValue(String value) {
		String oldValue = this.value;
		if (value != oldValue && (oldValue == null || !oldValue.equals(value))) {
			this.value = value;
			Radio checkRadio = null;
			for(EnumRadio enumRadio: enumRadioList){
				if(enumRadio.getCodeEnum().toCode().equals(value)){
					enumRadio.getRadio().setValue(true);
					checkRadio = enumRadio.getRadio();
				}else{
					enumRadio.getRadio().setValue(false);
				}
			}
			if(checkRadio != null){
				toggle.setValue(checkRadio);
			}
			ValueChangeEvent.fire(this, value);
		}
	}

	@Override
	public void reset() {
		if(resetEmpty){
			this.value = null;
			toggle.reset();
		}else{
			setValue(defaultEnum.toCode());
		}
	}
	
	/**
	 * 当需要用户强制选择一个的时候，执行clear逻辑并设置校验非空 
	 */
	@Override
	public void clear() {
		for(EnumRadio enumRadio: enumRadioList){
			enumRadio.getRadio().setValue(false);
		}
		this.value = null;
	}
	

}
