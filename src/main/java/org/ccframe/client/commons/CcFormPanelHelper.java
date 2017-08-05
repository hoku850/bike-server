package org.ccframe.client.commons;

import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.IsField;

public class CcFormPanelHelper extends FormPanelHelper {

	/**
	 * clearInvalid all field values.
	 */
	public static void clearInvalid(HasWidgets container) {
		for (IsField<?> f : getFields(container)) {
			f.clearInvalid();
		}
	}

	/**
	 * 支持忽略组件的reset，用于CcLabelValue系列联动的初始化
	 * @param container
	 * @param ignoreFields
	 */
	public static void reset(HasWidgets container, Field<?> ... ignoreFields) {
		for (IsField<?> f : getFields(container)) {
			boolean ignoreFlag = false;
			for(Field<?> ignoreField: ignoreFields){
				if(f == ignoreField){
					ignoreFlag = true;
					break;
				}
			}
			if(ignoreFlag){
				continue;
			}
			f.reset();
		}
	}


}
