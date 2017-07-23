package org.ccframe.subsys.core.dto;

import org.ccframe.subsys.core.domain.entity.Org;

public class OrgRowDto extends Org {

	/**
	 * 
	 */
	private static final long serialVersionUID = 565041499666817293L;

	private Double chargeTotalValue; // 充值总金额
	private Integer cyclingNum; // 骑行订单树
	private Double cyclingIncome; // 骑行总收入

	public Double getChargeTotalValue() {
		return chargeTotalValue;
	}

	public void setChargeTotalValue(Double chargeTotalValue) {
		this.chargeTotalValue = chargeTotalValue;
	}

	public Integer getCyclingNum() {
		return cyclingNum;
	}

	public void setCyclingNum(Integer cyclingNum) {
		this.cyclingNum = cyclingNum;
	}

	public Double getCyclingIncome() {
		return cyclingIncome;
	}

	public void setCyclingIncome(Double cyclingIncome) {
		this.cyclingIncome = cyclingIncome;
	}

}
