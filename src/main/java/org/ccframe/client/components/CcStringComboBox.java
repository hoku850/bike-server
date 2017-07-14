package org.ccframe.client.components;

import java.util.Arrays;

import org.ccframe.client.Global;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.widget.core.client.form.StringComboBox;

/**
 * StringComboBox的扩展，支持XML预设备选值.
 * @author JIM
 *
 */
public class CcStringComboBox extends StringComboBox {
	
	public CcStringComboBox(){
		super();
		this.setTriggerAction(TriggerAction.ALL);
	}
	
	public void setItemValues(String itemValuesStr){
		String[] itemValues = itemValuesStr.split(Global.ENUM_TEXT_SPLIT_CHAR);
		this.add(Arrays.asList(itemValues));
	}
}
