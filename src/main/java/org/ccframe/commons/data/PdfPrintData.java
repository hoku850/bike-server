package org.ccframe.commons.data;

import com.itextpdf.text.Font;

public class PdfPrintData {

	public static enum HorizontalAlign{
		LEFT, CENTER, RIGHT
	}
	

	/**
	 * 默认12px
	 */
	private Integer fontSize = Font.DEFAULTSIZE;
	/**
	 * 是否粗体
	 */
	private boolean bold;
	/**
	 * 是否斜体
	 */
	private boolean italic;
	/**
	 * 对齐方式
	 */
	private HorizontalAlign horizontalAlign = HorizontalAlign.LEFT;
	/**
	 * 高度，null不设置则为自动高
	 */
	private Integer height;
	/**
	 * 文本值
	 */
	private String value;
	/**
	 * 文字间距（暂未支持）
	 */
	private int fontspace = 0;

	protected PdfPrintData() {}
	
	/**
	 * 常见的属性构造方法，bold和italic、间距请创建后再set
	 * @param fontsize
	 * @param horizontalAlign
	 * @param height
	 * @param value
	 */
	public PdfPrintData(Integer fontSize, HorizontalAlign horizontalAlign, Integer height, String value){
		if(fontSize != null){
			this.fontSize = fontSize;
		}
		if(height != null){
			this.height = height;
		}
		this.horizontalAlign = horizontalAlign;
		this.value = value;
	}

	public int getFontspace() {
		return fontspace;
	}

	public void setFontspace(int fontspace) {
		this.fontspace = fontspace;
	}

	public Integer getFontSize() {
		return fontSize;
	}

	public void setFontSize(Integer fontSize) {
		this.fontSize = fontSize;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public HorizontalAlign getHorizontalAlign() {
		return horizontalAlign;
	}

	public void setHorizontalAlign(HorizontalAlign horizontalAlign) {
		this.horizontalAlign = horizontalAlign;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public boolean isItalic() {
		return italic;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}