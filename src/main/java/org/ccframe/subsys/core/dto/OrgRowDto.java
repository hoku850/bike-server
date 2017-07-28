package org.ccframe.subsys.core.dto;

import org.ccframe.subsys.core.domain.entity.Org;

public class OrgRowDto extends Org {

	/**
	 * 
	 */
	private static final long serialVersionUID = 565041499666817293L;

	private Double chargeTotalValue; // 充值总金额
	private Long cyclingNum; // 骑行订单数
	private Double cyclingIncome; // 骑行总收入

	public Double getChargeTotalValue() {
		return chargeTotalValue;
	}

	public void setChargeTotalValue(Double chargeTotalValue) {
		this.chargeTotalValue = chargeTotalValue;
	}

	public Long getCyclingNum() {
		return cyclingNum;
	}

	public void setCyclingNum(Long cyclingNum) {
		this.cyclingNum = cyclingNum;
	}

	public Double getCyclingIncome() {
		return cyclingIncome;
	}

	public void setCyclingIncome(Double cyclingIncome) {
		this.cyclingIncome = cyclingIncome;
	}

}
