package org.ccframe.commons.data;

/**
 * pdf打印单元格
 * @author JIM 
 *
 */
public class PdfCellPrintData extends PdfPrintData {
	
	private static final float DEFAULT_TOP_BOTTOM_PADDING = 5.0f;

	private static final float DEFAULT_LEFT_RIGHT_PADDING = 5.0f;

	public static enum VerticalAlign{
		TOP, MIDDLE, BOTTOM
	}
	
	public static enum BorderMode{
		/**
		 * 无边框
		 */
		NONE, 
		/**
		 * 细边框
		 */
		THIN, 
		/**
		 * 粗边框
		 */
		THICK, 
		/**
		 * 细底线，其它无边框
		 */
		BOTTOM_THIN, 
		/**
		 * 粗底线，其它无边框
		 */
		BOTTOM_THICK, 
		/**
		 * 虚线底线，一般用于间隔数据
		 */
		BOTTOM_DASH
	}
	
	/**
	 * 边框大小，0为无边框
	 */
	private BorderMode borderMode;
	
	private VerticalAlign verticalAlign;

	private Integer colspan;
	
	private Integer rowspan;
	
	private Float topBottomPadding = DEFAULT_TOP_BOTTOM_PADDING;
	
	private Float leftRightPadding = DEFAULT_LEFT_RIGHT_PADDING;
	
	/**
	 * 常用的单元格左对齐垂直居中的方式.
	 * @param borderWidth
	 * @param value
	 */
	public PdfCellPrintData(BorderMode borderMode, Integer colspan, String value){
		this(HorizontalAlign.LEFT, VerticalAlign.MIDDLE, borderMode, null, colspan, null,  null, value);
	}
	
	public PdfCellPrintData(HorizontalAlign horizontalAlign, VerticalAlign verticalAlign, BorderMode borderMode, Integer height, Integer colspan, Integer rowspan, Integer fontSize, String value){
		this(horizontalAlign, verticalAlign, borderMode, height, colspan, rowspan, fontSize, null, null, value);
	}

	public PdfCellPrintData(HorizontalAlign horizontalAlign, VerticalAlign verticalAlign, BorderMode borderMode, Integer height, Integer colspan, Integer rowspan, Integer fontSize, Float topBottomPadding, Float leftRightPadding, String value){
		super(fontSize, horizontalAlign, height, value);
		this.verticalAlign = verticalAlign;
		this.borderMode = borderMode;
		this.rowspan = rowspan;
		this.colspan = colspan;
		if(topBottomPadding != null){
			this.topBottomPadding = topBottomPadding;
		}
		if(leftRightPadding != null){
			this.leftRightPadding = leftRightPadding;
		}
	}

	public BorderMode getBorderMode() {
		return borderMode;
	}

	public void setBorderMode(BorderMode borderMode) {
		this.borderMode = borderMode;
	}

	public VerticalAlign getVerticalAlign() {
		return verticalAlign;
	}

	public void setVerticalAlign(VerticalAlign verticalAlign) {
		this.verticalAlign = verticalAlign;
	}

	public Integer getColspan() {
		return colspan;
	}

	public void setColspan(Integer colspan) {
		this.colspan = colspan;
	}

	public Integer getRowspan() {
		return rowspan;
	}

	public void setRowspan(Integer rowspan) {
		this.rowspan = rowspan;
	}

	public Float getTopBottomPadding() {
		return topBottomPadding;
	}

	public void setTopBottomPadding(Float topBottomPadding) {
		this.topBottomPadding = topBottomPadding;
	}

	public Float getLeftRightPadding() {
		return leftRightPadding;
	}

	public void setLeftRightPadding(Float leftRightPadding) {
		this.leftRightPadding = leftRightPadding;
	}
	
	
}
