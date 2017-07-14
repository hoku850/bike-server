/*
 * $HeadURL: $
 * $Id: $
 * Copyright (c) 2010 by Ericsson, all rights reserved.
 */

package org.ccframe.client.base;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class BaseEvent<H extends EventHandler> extends GwtEvent<H> {

    @Override
    protected void dispatch(H handler) {
        ((BaseHandler) handler).action(this);
    }
}
