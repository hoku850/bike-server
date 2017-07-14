package org.ccframe.client.components;


import java.util.Date;

import org.ccframe.client.commons.UtilDateTimeClient;

import com.sencha.gxt.widget.core.client.form.AdapterField;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;

/**
 * 时间组件，与标准类型绑定Data不同，此类型绑定的是String类型Value.
 * 能够方便的绑定domain的时间扩展String字段
 * 
 * @author JIM
 *
 */
public class CcDateField extends AdapterField<String>{

	private DateField dateField;
	
	public CcDateField(){
		super(new DateField(new DateTimePropertyEditor(UtilDateTimeClient.DATE_PATTERN)));
		dateField = (DateField) getWidget();

	}
	
	@Override
	public void setValue(String value) {
		dateField.setValue(UtilDateTimeClient.convertStringToDateTime(value));
	}

	@Override
	public String getValue() {
		return dateField.getText();
	}

	public void setAllowBlank(boolean allowBlank) {
		dateField.setAllowBlank(allowBlank);
	}
	
	@Override
	public void reset(){
		dateField.getDatePicker().setValue(new Date(), false);
	}
}
