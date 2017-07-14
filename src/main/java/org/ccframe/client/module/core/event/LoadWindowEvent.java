package org.ccframe.client.module.core.event;

import org.ccframe.client.ViewResEnum;
import org.ccframe.client.base.BaseEvent;
import org.ccframe.client.commons.WindowEventCallback;

import com.google.gwt.event.shared.EventHandler;

public class LoadWindowEvent<ReqType, RetType, H extends EventHandler> extends BaseEvent<H>{

	public static final Type TYPE = new Type();

    private ViewResEnum viewResEnum;

    private ReqType reqObject;
    
    private WindowEventCallback<RetType> callback;

    /**
     * 弹出并关心回调的窗体.
     * @param presenterResEnum
     * @param objectId
     * @param callback
     */
    public LoadWindowEvent(ViewResEnum viewResEnum, ReqType reqObject, WindowEventCallback<RetType> callback){
    	this.viewResEnum = viewResEnum;
    	this.reqObject = reqObject;
    	this.callback = callback;
    }

	@Override
	public Type<H> getAssociatedType() {
		return TYPE;
	}
	
	public ViewResEnum getViewResEnum(){
		return viewResEnum;
	}
	
    public ReqType getReqObject() {
		return reqObject;
	}

	public WindowEventCallback<RetType> getCallback() {
		return callback;
	}

}
