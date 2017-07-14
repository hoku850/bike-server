package org.ccframe.client.commons;

/**
 * 窗体关闭后返回的数据.
 * 如果需要远程校验数据，应放到窗体“完成”等按钮的交互里，不要作为消息回调事件！
 * 
 * @author Jim
 *
 * @param <RT> 窗体关闭时返回的数据
 */
public interface WindowEventCallback<RT> {
	void onClose(RT returnData);
}
