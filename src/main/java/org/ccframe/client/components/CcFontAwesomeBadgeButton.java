package org.ccframe.client.components;

import com.google.gwt.uibinder.client.UiConstructor;

/**
 * Iconfont图标库按钮
 * @author JIM
 */
public class CcFontAwesomeBadgeButton extends CcFlatIconButton{

	@UiConstructor
	public CcFontAwesomeBadgeButton(String badgeText, String iconName) {
		super(badgeText, iconName);
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
