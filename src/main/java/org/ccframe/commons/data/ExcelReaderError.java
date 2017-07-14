package org.ccframe.commons.data;


/**
 * 异常错误信息，用于在EXCEL导入后.
 * 将针对导出结果EXCEL进行备注信息的标记
 * 
 * @author Jim
 */
public class ExcelReaderError {
	private int col;
	private int row;
	private String errorText;
	public ExcelReaderError(){}
	public ExcelReaderError(int col, int row, String errorText){
		this.col = col;
		this.row = row;
		this.errorText = errorText;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public String getErrorText() {
		return errorText;
	}
	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	
}
