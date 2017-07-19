package org.ccframe.client.components;

import org.ccframe.client.Global;

import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

public class CcEmailField extends CcTextField{

	public CcEmailField(){
		this.addValidator(new RegExValidator(Global.EMAIL_PATTERN, "E-MAIL格式错误"));
		this.setMaxLength(64);
	}

}
