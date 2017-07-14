package org.ccframe.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ClientImages extends ClientBundle {
	public ClientImages INSTANCE = GWT.create(ClientImages.class);

	//菜单资源
	
	//标准按钮资源
	
	@Source("add.gif")
	ImageResource add16();
	
}
