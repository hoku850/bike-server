package org.ccframe.commons.data;

/**
 * @author JIM
 *
 */
public class PdfAreaPrintData extends PdfPrintData {
	
	/**
	 * 套打顶部位置
	 */
	private Integer top;
	/**
	 * 套打坐偏移位置
	 */
	private Integer left;
	/**
	 * 套打的区块宽
	 */
	private Integer width;
	
	/**
	 * 禁止直接new
	 */
	protected PdfAreaPrintData(){};
	
	public PdfAreaPrintData(Integer left, Integer top, Integer width, Integer height, Integer fontsize, String value){
		super(fontsize, HorizontalAlign.LEFT, height, value);

		this.top = top;
		this.left = left;
		this.width = width;
	}
	
	public Integer getTop() {
		return top;
	}
	public void setTop(Integer top) {
		this.top = top;
	}
	public Integer getLeft() {
		return left;
	}
	public void setLeft(Integer left) {
		this.left = left;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

}
