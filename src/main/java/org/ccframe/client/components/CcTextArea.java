package org.ccframe.client.components;

import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;

public class CcTextArea extends TextArea{
	
	public void setMinLength(int minlength){
		this.addValidator(new MinLengthValidator(minlength));
	}
	
	public void setMaxLength(int maxLength){
		getCell()
	    .getAppearance()
	    .getInputElement(getElement())
	    .setAttribute("maxLength", Integer.toString(maxLength));
	}
}
