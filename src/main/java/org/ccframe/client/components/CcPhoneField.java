package org.ccframe.client.components;

import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

public class CcPhoneField extends CcTextField{

	private static final String PHONE_CHECK_REGEX = "^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{1,4})|(\\d{7,8})-(\\d{1,4}))$";
	
	public CcPhoneField(){
		this.addValidator(new RegExValidator(PHONE_CHECK_REGEX, "电话号码格式错误"));
		this.setMaxLength(18);
	}

}
