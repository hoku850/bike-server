package org.ccframe.sdk.bike.dto;

public class Discount {
	private String disName;
	private String disRemark;
	private String disPrice;
	
	public Discount(String disName, String disRemark, String disPrice) {
		super();
		this.disName = disName;
		this.disRemark = disRemark;
		this.disPrice = disPrice;
	}

	public String getDisName() {
		return disName;
	}

	public void setDisName(String disName) {
		this.disName = disName;
	}

	public String getDisRemark() {
		return disRemark;
	}

	public void setDisRemark(String disRemark) {
		this.disRemark = disRemark;
	}

	public String getDisPrice() {
		return disPrice;
	}

	public void setDisPrice(String disPrice) {
		this.disPrice = disPrice;
	}

	
}
