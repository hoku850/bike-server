package org.ccframe.client.components;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Text;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;

/**
 * 平面三态图标按钮，支持badge按钮.需配合iconfont使用
 * 如果要构造支持badgeText的button，一定要指定badgeText
 * @author JIM
 *
 */
public abstract class CcFlatIconButton extends Button{

	private Text faTextNode;
	private Element liNode;
	protected Document document;

	private Element badgeSpan; 
	private Text badgeTextNode;

	private String iconName;
	private String overStyleName;
	private String disableStyleName;
	
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

	public CcFlatIconButton(String badgeText, String iconName){
		this(iconName);
		badgeSpan = document.createElement("span");
		badgeSpan.addClassName("badge");
		badgeTextNode = document.createTextNode(badgeText);
		badgeSpan.appendChild(badgeTextNode);
		getElement().appendChild(badgeSpan);
	}
	
	public CcFlatIconButton(String iconName){
		document = getElement().getOwnerDocument(); 
		faTextNode = document.createTextNode("");
		liNode = document.createElement("li");
		getElement().appendChild(liNode);
		getElement().appendChild(faTextNode);
		this.setIconName(iconName);

	    sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
	}

	public void setBadgeStyleName(String badgeStyleName) {
		if(badgeTextNode != null){
			badgeSpan.addClassName(badgeStyleName);
		}
	}

	public void setOverStyleName(String overStyleName) {
		this.overStyleName = overStyleName;
	}

	public void setDisableStyleName(String disableStyleName) {
		this.disableStyleName = disableStyleName;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		if(iconName != null){
			liNode.removeClassName(getFontClassPerfix() + iconName);
		}
		liNode.addClassName(getFontFamilyClass());
		liNode.addClassName(getFontClassPerfix() + iconName);
	}

	public void setBadgeText(String badgeText) {
		if(badgeTextNode != null){
			badgeTextNode.setData(badgeText);
		}
	}

	public String getBadgeText() {
		return badgeTextNode.getData();
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if(disableStyleName != null){
			if(enabled){
				removeStyleName(disableStyleName);
			}else{
				addStyleName(disableStyleName);
			}
		}
	}
	
	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		switch (event.getTypeInt()) {
		case Event.ONMOUSEOVER:
			if (isEnabled()) {
				if(overStyleName != null){
					addStyleName(overStyleName);
				}
			}
			break;
		case Event.ONMOUSEOUT:
			if(overStyleName != null){
				removeStyleName(overStyleName);
			}
			break;
		case Event.ONCLICK:
			click();
			break;
		}
	}

}
