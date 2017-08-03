package org.ccframe.client.components;

import com.google.gwt.uibinder.client.UiConstructor;

/**
 * Iconfont图标库按钮
 * @author JIM
 */
public class CcFontAwesomeButton extends CcFlatIconButton{

	@UiConstructor
	public CcFontAwesomeButton(String badgeText, String iconName) {
		super(badgeText, iconName);
	}

	@UiConstructor
	public CcFontAwesomeButton(String iconName) {
		super(iconName);
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
