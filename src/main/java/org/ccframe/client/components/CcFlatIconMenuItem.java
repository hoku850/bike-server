package org.ccframe.client.components;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public abstract class CcFlatIconMenuItem extends MenuItem {

	private Element liNode;
	
	/**
	 * 定义字体样式的类. 例如fa,iconfont
	 * @return
	 */
	protected abstract String getFontFamilyClass();
	/**
	 * 字体类前缀. 例如fa-,icon-
	 * @return
	 */
	protected abstract String getFontClassPerfix();

	public CcFlatIconMenuItem(String text, String iconName){
		super(text);
		liNode = DOM.createElement("li");
		liNode.addClassName(getFontFamilyClass());
		liNode.addClassName(getFontClassPerfix() + iconName);
		liNode.addClassName("cc-menu-icon");

		XElement anchor = XElement.as(getElement().getFirstChild());
		anchor.insertChild(liNode, 0);
	}

	public void setIconName(String iconName) {
		if(iconName != null){
			liNode.removeClassName(getFontClassPerfix() + iconName);
		}
		liNode.addClassName(getFontFamilyClass());
		liNode.addClassName(getFontClassPerfix() + iconName);
	}
}
