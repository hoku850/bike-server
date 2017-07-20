package org.ccframe.client.components;

import com.google.gwt.dom.client.InputElement;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;

public class CcTextArea extends TextArea{
	
	private Integer maxLength;

	public void setMinLength(int minlength){
		this.addValidator(new MinLengthValidator(minlength));
	}

	public void setMaxLength(int maxLength){
		this.maxLength = maxLength;
	}

	@Override
	public void setValue(String value){
		super.setValue(value);
		if(this.isRendered()){
			InputElement ie = getCell().getAppearance().getInputElement(getElement()).cast();
			ie.setMaxLength(maxLength);
		}
	}
}
