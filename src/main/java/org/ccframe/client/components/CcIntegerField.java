package org.ccframe.client.components;

import com.google.gwt.dom.client.InputElement;
import com.sencha.gxt.widget.core.client.form.IntegerField;

/**
 * 整数输入框，支持默认值和maxlength(发布模式).
 * @author JIM
 *
 */
public class CcIntegerField extends IntegerField{
	
	private Integer defaultValue;
	
	public CcIntegerField() {
		super();
		setMaxLength(10); //默认长度10
	}
	
	public void setDefaultValue(int defaultValue){
		this.defaultValue = defaultValue;
	}
	
	public void setMaxLength(int maxLength){
		InputElement ie = getCell()
	    .getAppearance()
	    .getInputElement(getElement()).cast();
		ie.setMaxLength(maxLength);
	}
	
	@Override
	public void reset(){
		this.clearInvalid();
		if(defaultValue != null){
			this.setValue(defaultValue);
		}else{
			super.reset();
		}
	}
}
