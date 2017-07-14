package org.ccframe.client.components;

/**
 * @author JIM
 * 动态绑定关联ID值用的数据结构。
 * label是显示的名称，value是绑定的值，一般是ID。
 */
public class LabelValue {
	private String label;
	private Integer value;
	
	public LabelValue(){};
	
	public LabelValue(String label, Integer value){
		this.label = label;
		this.value = value;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
}
