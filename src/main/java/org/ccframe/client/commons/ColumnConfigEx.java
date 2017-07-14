package org.ccframe.client.commons;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;

public class ColumnConfigEx<M, N> extends ColumnConfig<M, N> {

	public ColumnConfigEx(ValueProvider<? super M, N> valueProvider, int width, String header, HorizontalAlignmentConstant alignment, boolean fixed) {
		super(valueProvider, width, header);
		this.setMenuDisabled(true);
		this.setSortable(false);
		this.setHorizontalAlignment(alignment);
		this.setFixed(fixed);
	}


}
