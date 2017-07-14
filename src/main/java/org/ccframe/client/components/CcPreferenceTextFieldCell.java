package org.ccframe.client.components;

import java.util.List;

import org.ccframe.client.ViewResEnum;
import org.ccframe.client.commons.ClientManager;
import org.ccframe.client.commons.EventBusUtil;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.commons.WindowEventCallback;
import org.ccframe.client.module.core.event.LoadWindowEvent;
import org.fusesource.restygwt.client.Method;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventHandler;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;

/**
 * 预设文本组件，支持与某个系统参数关联，实现多个文本的选择.
 * useManageItem 默认开启，开启后可以在界面管理预设文本
 * @author JIM
 *
 */
public class CcPreferenceTextFieldCell extends TriggerFieldCell<String>{
	private Menu menu;
	private boolean expanded;
	/**
	 * 数据库参数表的内部编码名称，必须非空
	 */
	private String preferenceParamInnerCoding;
	private boolean useManageItem;
	private boolean reloading;

	public CcPreferenceTextFieldCell(){
		menu = new Menu();
		menu.addHideHandler(new HideHandler() {
			@Override
			public void onHide(HideEvent event) {
				collapse(lastContext, lastParent);
			}
		});
	}
	
	public void updateText(String text){
		getInputElement(lastParent).setValue(text);
	}

	public void collapse(final Context context, final XElement parent) {
		if (!expanded) {
			return;
		}

		expanded = false;

		menu.hide();
		fireEvent(context, new CollapseEvent(context));
	}

	@Override
	protected void onTriggerClick(Context context, XElement parent, NativeEvent event, String value, ValueUpdater<String> updater) {
		super.onTriggerClick(context, parent, event, value, updater);
		if (!isReadOnly() && !isDisabled()) {
			// blur is firing after the expand so context info on expand is
			// being cleared
			// when value change fires lastContext and lastParent are null
			// without this code
			if ((GXT.isWebKit()) && lastParent != null && lastParent != parent) {
				getInputElement(lastParent).blur();
			}
			onFocus(context, parent, value, event, updater);
			expand(context, parent, value, updater);
		}
	}

	public void expand(final Context context, final XElement parent,String value, ValueUpdater<String> valueUpdater) {
		if (expanded || reloading) {
			return;
		}

		this.expanded = true;

		// expand may be called without the cell being focused
		// saveContext sets focusedCell so we clear if cell
		// not currently focused
		boolean focused = focusedCell != null;
		saveContext(context, parent, null, valueUpdater, value);
		if (!focused) {
			focusedCell = null;
		}

		menu.getElement().getStyle().clearWidth(); //每次自动计算菜单宽度
		menu.show(parent, new AnchorAlignment(Anchor.TOP_RIGHT, Anchor.BOTTOM_RIGHT, true));
		fireEvent(context, new ExpandEvent(context));
	}

	@Override
	protected boolean isFocusedWithTarget(Element parent, Element target) {
		boolean result = (menu != null && (menu.getElement().isOrHasChild(target))) || parent.isOrHasChild(target);
		return result;
	}

	@Override
	protected void triggerBlur(Context context, XElement parent, String value,	ValueUpdater<String> valueUpdater) {
		super.triggerBlur(context, parent, value, valueUpdater);
		collapse(context, parent);
	}

	public void setPreferenceParamInnerCoding(String preferenceParamInnerCoding) {
		this.preferenceParamInnerCoding = preferenceParamInnerCoding;
	}

	public void setUseManageItem(boolean useManageItem) {
		this.useManageItem = useManageItem;
	}


	public void reloadPreferenceMenuItem() {
		if(preferenceParamInnerCoding == null || reloading == true){
			return;
		}
		menu.clear();
		if(useManageItem){
			menu.add(new MenuItem("<管理预设>", new SelectionHandler<MenuItem>(){
				@Override
				public void onSelection(SelectionEvent<MenuItem> event) {
					EventBusUtil.fireEvent(new LoadWindowEvent<String, Void, EventHandler>(ViewResEnum.PREFERENCE_TEXT_EDIT_WINDOW, preferenceParamInnerCoding, new WindowEventCallback<Void>(){
						@Override
						public void onClose(Void returnData) {
							reloadPreferenceMenuItem();
						}
					}));
				}
			}));
			menu.add(new SeparatorMenuItem());
		}
		reloading = true;
		ClientManager.getParamClient().getPreferenceText(preferenceParamInnerCoding, new RestCallback<List<String>>(){
			@Override
			public void onSuccess(Method method, List<String> response) {
				for(String itemText: response){
					CcPreferenceTextFieldCell.this.menu.add(new MenuItem(itemText, new SelectionHandler<MenuItem>(){
						@Override
						public void onSelection(SelectionEvent<MenuItem> event) {
							CcPreferenceTextFieldCell.this.updateText(event.getSelectedItem().getText());
						}
					}));
				}
				reloading = false;
			}

			@Override
			protected void afterFailure() {
				reloading = false;
			}
			
		});
	}

	
}
