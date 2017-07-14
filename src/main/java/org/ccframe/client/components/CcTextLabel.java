package org.ccframe.client.components;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;


public class CcTextLabel extends Label{
	public CcTextLabel(){}
	@Override
	public void setWidth(String width){
		super.setWidth(width);
		this.getElement().getStyle().setWidth(Double.parseDouble(width), Unit.PX);
	}
}
