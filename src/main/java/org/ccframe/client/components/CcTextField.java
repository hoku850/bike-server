package org.ccframe.client.components;

import com.google.gwt.dom.client.InputElement;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;

public class CcTextField extends TextField{
	
	public void setMinLength(int minlength){
		this.addValidator(new MinLengthValidator(minlength));
	}
	
	public void setMaxLength(int maxLength){
		InputElement ie = getCell()
	    .getAppearance()
	    .getInputElement(getElement()).cast();
		ie.setMaxLength(maxLength);
	}
}
