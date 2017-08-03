package org.ccframe.client.components;

public class CcIconfontMenuItem extends CcFlatIconMenuItem {

	public CcIconfontMenuItem(String text, String iconName) {
		super(text, iconName);
	}

	@Override
	protected String getFontFamilyClass() {
		return "iconfont";
	}

	@Override
	protected String getFontClassPerfix() {
		return "icon-";
	}
}
