package org.ccframe.client.components;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.HasCollapseHandlers;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.HasExpandHandlers;
import com.sencha.gxt.widget.core.client.form.TriggerField;

/**
 * 可以有下拉默认文本菜单选择的组件.菜单支持默认文本的编辑。
 * @author JIM
 *
 */
public class CcPreferenceTextField extends TriggerField<String> implements HasExpandHandlers, HasCollapseHandlers{

	public CcPreferenceTextField(){
		this(new CcPreferenceTextFieldCell());
	}
	
	protected CcPreferenceTextField(TriggerFieldCell<String> cell) {
		super(cell);
	}
	@Override
	public HandlerRegistration addCollapseHandler(CollapseHandler handler) {
		return addHandler(handler, CollapseEvent.getType());
	}
	@Override
	public HandlerRegistration addExpandHandler(ExpandHandler handler) {
		return addHandler(handler, ExpandEvent.getType());
	}

	@Override
	public CcPreferenceTextFieldCell getCell() {
		return (CcPreferenceTextFieldCell) super.getCell();
	}

	protected void expand() {
		getCell().expand(createContext(), getElement(), getValue(), valueUpdater);
	}

	@Override
	public void reset() {
		//从服务器load文本菜单
		reloadPreferenceMenuItem();
		super.reset();
	}

	public void reloadPreferenceMenuItem(){
		getCell().reloadPreferenceMenuItem();
	}
	
	/**
	 * 默认文本对应的系统参数名.
	 * @param preferenceParamInnerCoding
	 */
	public void setPreferenceParamInnerCoding(String preferenceParamInnerCoding) {
		getCell().setPreferenceParamInnerCoding(preferenceParamInnerCoding);
	}

	/**
	 * 是否使用管理预设的默认组件.
	 * @param preferenceParamInnerCoding
	 */
	public void setUseManageItem(boolean useManageItem) {
		getCell().setUseManageItem(useManageItem);
	}

}
