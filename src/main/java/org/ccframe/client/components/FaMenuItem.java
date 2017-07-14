package org.ccframe.client.components;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class FaMenuItem extends MenuItem {

	private static final String FA_MENU_ICON_CLASS = "fa-menu-icon";
	
	private FaIconType faIconType;

	public FaMenuItem(String text){
		super(text);
	}
	
	public void setFaIconType(FaIconType faIconType) {
		this.faIconType = faIconType;
		XElement anchor = XElement.as(getElement().getFirstChild());
		XElement oldFaIcon = getElement().selectNode("." + FA_MENU_ICON_CLASS);
		if (oldFaIcon != null) {
			oldFaIcon.removeFromParent();
		}
		if (faIconType != null) {
			Element e = DOM.createElement("li");
			e.addClassName(FA_MENU_ICON_CLASS);
			e.addClassName("fa");
			e.addClassName(faIconType.toCode());
			anchor.insertChild(e, 0);
		}
	}

	public FaIconType getFaIconType(){
		return faIconType;
	}
}
