package org.ccframe.client.module.core.view;

import com.google.gwt.user.client.ui.Label;
import com.google.inject.Singleton;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

@Singleton
public class HelloView extends SimpleContainer {
	public HelloView(){
		this.add(new Label("hello world."));
	}
}
