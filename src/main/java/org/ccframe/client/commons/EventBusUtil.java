package org.ccframe.client.commons;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.web.bindery.event.shared.Event.Type;

public class EventBusUtil {
	private static EventBus eventBus = GWT.create(SimpleEventBus.class);
	public static void fireEvent(GwtEvent<?> event){
		eventBus.fireEvent(event);
	}
	public static void addHandler(Type type, Object handler){
		eventBus.addHandler(type, handler);
	}
}
