package org.ccframe.client.components;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * TODO 树结构级联的设置框.
 * 考虑采用装饰器模式形成链式响应结构
 * @author JIM
 *
 */
public class CcTreeComboLinkField extends ComboBox<Integer>{

	public CcTreeComboLinkField(ComboBoxCell<Integer> cell) {
		super(cell);
	}

}
