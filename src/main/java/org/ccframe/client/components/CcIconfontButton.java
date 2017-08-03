package org.ccframe.client.components;

import com.google.gwt.uibinder.client.UiConstructor;

/**
 * Iconfont图标库按钮
 * @author JIM
 */
public class CcIconfontButton extends CcFlatIconButton{

	@UiConstructor
	public CcIconfontButton(String iconName) {
		super(iconName);
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
