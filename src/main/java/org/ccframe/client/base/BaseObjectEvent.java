package org.ccframe.client.base;

import org.ccframe.client.ViewResEnum;

@SuppressWarnings("rawtypes")
public abstract class BaseObjectEvent<T> extends BaseEvent<BaseHandler> {

    private T object;

    /**
     * 记录了跳转的界面由哪个模块跳转而来
     */
    private ViewResEnum lastViewResEnum;

    public BaseObjectEvent(T object){
    	this.object = object;
    }

	public abstract Type<BaseHandler> getAssociatedType();

	public T getObject() {
		return object;
	}

}
