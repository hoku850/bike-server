package org.ccframe.client.commons;

import com.google.gwt.user.client.ui.HasWidgets;
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

}
