package org.ccframe.client.components;

import org.ccframe.subsys.core.domain.code.BoolCodeEnum;

public class CcBoolRadioField extends CcEnumRadioField{
	
	private String yesText;
	private String noText;
	
	public void setDefaultValue(BoolCodeEnum defaultBool){
		super.setDefault(defaultBool);
	}
	
	public void setYesText(String yesText){
		this.yesText = yesText;
	}
	
	public void setNoText(String noText){
		this.noText = noText;
	}
	
	@Override
	protected void onAfterFirstAttach() {
		this.setEnumTexts((yesText == null ? "是" : yesText) + "," + (noText == null ? "否" : noText));
		super.onAfterFirstAttach();
	}

	public Boolean getBoolValue() {
		String value = this.getValue(); 
		return value == null ? null : BoolCodeEnum.YES.toCode().equals(value);
	}

	public void setBoolValue(boolean value){
		this.setValue(value ? BoolCodeEnum.YES.toCode(): BoolCodeEnum.NO.toCode());
	}
}
