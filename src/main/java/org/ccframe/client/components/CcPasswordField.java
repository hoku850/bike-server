package org.ccframe.client.components;

import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;

public class CcPasswordField extends PasswordField{
	
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
