package org.ccframe.client.base;

import org.ccframe.client.commons.ICcWindow;
import org.ccframe.client.commons.WindowEventCallback;
import org.ccframe.client.module.core.event.LoadWindowEvent;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * 
 * 
 * @author JIM
 *
 * @param <T>
 */
public abstract class BaseWindowView<ReqType, RetType> implements ICcWindow<ReqType, RetType>{

	public Widget widget;
	
	/**
	 * 在弹窗任何时间需要返回数据时，调用此callback方法
	 */
	protected WindowEventCallback<RetType> retCallBack;

	@UiField
	public Window window;
	
	@UiHandler("closeButton")
	public void handleCloseClick(SelectEvent e){
		window.hide();
	}

	@Override
	public Widget asWidget() {
		if(widget == null){
			widget = bindUi();
		}
		return widget;
	}

	abstract protected Widget bindUi();
	
	abstract protected void onLoadData(ReqType reqType);
	
	@Override
	public void show(LoadWindowEvent<ReqType, RetType,? extends EventHandler> event) {
		window.show();
		window.center();
		onLoadData(event.getReqObject());
		retCallBack = event.getCallback();
	}
}
