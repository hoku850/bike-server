package org.ccframe.client.commons;

import org.ccframe.client.module.core.event.LoadWindowEvent;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.ui.IsWidget;


public interface ICcWindow<ReqType, RetType> extends IsWidget{
	/**
	 * 窗体的显示.
	 */
	void show(LoadWindowEvent<ReqType, RetType,? extends EventHandler> event);
}
