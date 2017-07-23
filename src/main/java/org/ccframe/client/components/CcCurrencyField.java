package org.ccframe.client.components;

import com.google.gwt.i18n.client.NumberFormat;
import com.sencha.gxt.widget.core.client.form.DoubleField;

/**
 * 金额组件，默认2位小数，只允许输入正数.
 * @author JIM
 *
 */
public class CcCurrencyField extends DoubleField{

	private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getFormat("0.00");

	public CcCurrencyField(){
		this.setAllowNegative(false); //默认不允许输入负数，但是可以在xml里设置
		this.setFormat(CURRENCY_FORMAT);
	}
	
	@Override
	public Double getValue(){
		return super.getValue() == null ? null : CURRENCY_FORMAT.parse(CURRENCY_FORMAT.format(super.getValue()));
	}
}
