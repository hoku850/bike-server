package org.ccframe.client.components;

import com.google.gwt.uibinder.client.UiConstructor;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

/**
 * 支持最小模式的分页bar，最小模式去掉了首页和下页按钮及页数跳转
 * @author JIM
 *
 */
public class CcPagingToolBar extends PagingToolBar {

	private boolean mininumMode;
	
	public void setMininumMode(boolean mininumMode) {
		this.mininumMode = mininumMode;
	}

	@UiConstructor
	public CcPagingToolBar(int pageSize) {
		super(pageSize);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		if(mininumMode){
//			first.hide();
//			last.hide();
//			pageText.setVisible(false);
			beforePage.hide();
			afterText.hide();
			displayText.hide();
		}
		this.forceLayout();
	}

}
