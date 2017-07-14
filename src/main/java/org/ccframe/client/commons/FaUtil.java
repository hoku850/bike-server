package org.ccframe.client.commons;

import org.ccframe.client.components.FaIconType;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;

public class FaUtil {
	private FaUtil(){}
	public static final Element createFaElement(Document document,FaIconType faIconType){
		final Element element = document.createElement("li");
		if(faIconType != null){
			element.addClassName("fa");
			element.addClassName(faIconType.toCode());
		}
		return element;
	}

	
}
