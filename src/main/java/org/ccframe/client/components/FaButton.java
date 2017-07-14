package org.ccframe.client.components;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Text;
import com.google.gwt.user.client.ui.Button;

public class FaButton extends Button{
	private Text faTextNode;
	private FaIconType faIconType = FaIconType.SQUARE_O;
	private Element liNode;
	protected Document document;

	public FaButton(){
		document = getElement().getOwnerDocument(); 
		faTextNode = document.createTextNode("");
		liNode = document.createElement("li");
		getElement().appendChild(liNode);
		getElement().appendChild(faTextNode);
	}
	
	public FaIconType getFaIconType() {
		return faIconType;
	}

	public void setFaIconType(FaIconType faIconType) {
		this.faIconType = faIconType;
		if(liNode.getClassName().length() > 0){
			liNode.removeClassName(liNode.getClassName());
		}
		liNode.addClassName("fa");
		liNode.addClassName(faIconType.toCode());
	}

	@Override
	public void setText(String text) {
		faTextNode.setData(text);
	}

	@Override
	public String getText() {
		return faTextNode.getData();
	}

}
