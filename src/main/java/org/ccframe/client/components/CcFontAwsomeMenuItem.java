package org.ccframe.client.components;

public class CcFontAwsomeMenuItem extends CcFlatIconMenuItem {

	public CcFontAwsomeMenuItem(String text, String iconName) {
		super(text, iconName);
	}

	@Override
	protected String getFontFamilyClass() {
		return "fa";
	}

	@Override
	protected String getFontClassPerfix() {
		return "fa-";
	}
}
