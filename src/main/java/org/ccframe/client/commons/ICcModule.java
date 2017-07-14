package org.ccframe.client.commons;

import org.ccframe.client.module.core.event.BodyContentEvent;

import com.google.gwt.user.client.ui.IsWidget;

public interface ICcModule extends IsWidget{
	/**
	 * 所有的模块重新加载的代码.
	 * 例如主显示区域的重新加载，列表重新刷新。窗体重新显示的下拉列表更新。
	 */
	void onModuleReload(BodyContentEvent event);
}
