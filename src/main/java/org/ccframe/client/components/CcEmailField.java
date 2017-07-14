package org.ccframe.client.components;

import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

public class CcEmailField extends CcTextField{

	private static final String EMAIL_CHECK_REGEX = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	
	public CcEmailField(){
		this.addValidator(new RegExValidator(EMAIL_CHECK_REGEX, "E-MAIL格式错误"));
		this.setMaxLength(64);
	}

}
