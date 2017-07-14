package org.ccframe.client.base;

import com.google.gwt.event.shared.EventHandler;

public interface BaseHandler<E> extends EventHandler {
	void action(E event);
}
