package org.ccframe.client.module.core.event;

import org.ccframe.client.ViewResEnum;
import org.ccframe.client.base.BaseEvent;
import org.ccframe.client.base.BaseHandler;

@SuppressWarnings("rawtypes")
public class BodyContentEvent extends BaseEvent<BaseHandler> {

	public static final Type TYPE = new Type();

    private ViewResEnum viewResEnum;
    
    private Object paramObj;

    /**
     * 记录了跳转的界面由哪个模块跳转而来
     */
    private ViewResEnum lastViewResEnum;

    public BodyContentEvent(ViewResEnum presenterResEnum, ViewResEnum lastViewResEnum,Object paramObj){
    	this.viewResEnum = presenterResEnum;
    	this.lastViewResEnum = lastViewResEnum;
    	this.paramObj = paramObj;
    }

    public BodyContentEvent(ViewResEnum presenterResEnum, Object paramObj){
    	this(presenterResEnum, null, paramObj);
    }

    public BodyContentEvent(ViewResEnum presenterResEnum){
    	this(presenterResEnum, null);   	
    }

    @Override
	public Type<BaseHandler> getAssociatedType() {
		return TYPE;
	}

	public ViewResEnum getViewResEnum() {
		return viewResEnum;
	}

	public void setViewResEnum(ViewResEnum viewResEnum) {
		this.viewResEnum = viewResEnum;
	}

	public ViewResEnum getLastViewResEnum() {
		return lastViewResEnum;
	}

	public void setLastViewResEnum(ViewResEnum lastViewResEnum) {
		this.lastViewResEnum = lastViewResEnum;
	}

	public Object getParamObj(){
		return paramObj;
	}
}
