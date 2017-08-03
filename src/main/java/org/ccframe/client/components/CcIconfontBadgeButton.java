package org.ccframe.client.components;

import com.google.gwt.uibinder.client.UiConstructor;

/**
 * Iconfont图标库按钮
 * @author JIM
 */
public class CcIconfontBadgeButton extends CcFlatIconButton{

	@UiConstructor
	public CcIconfontBadgeButton(String badgeText, String iconName) {
		super(badgeText, iconName);
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
