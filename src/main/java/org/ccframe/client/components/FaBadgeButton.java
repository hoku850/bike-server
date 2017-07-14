package org.ccframe.client.components;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Text;

public class FaBadgeButton extends FaButton{
	private Text badgeTextNode;

	public FaBadgeButton(){
		super();
		Element badgeSpan = document.createElement("span");
		badgeSpan.addClassName("badge");
		badgeTextNode = document.createTextNode("");
		badgeSpan.appendChild(badgeTextNode);
		getElement().appendChild(badgeSpan);
	}

	public void setBadgeText(String badgeText) {
		badgeTextNode.setData(badgeText);
	}

	public String getBadgeText() {
		return badgeTextNode.getData();
	}

}
