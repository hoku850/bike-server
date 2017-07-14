package org.ccframe.subsys.bike.dto;

import org.ccframe.subsys.bike.domain.entity.Agent;

public class AgentRowDto extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 565041499666817293L;

	private Double chargeTotalValue;
	private Integer cyclingNum;
	private Double cyclingIncome;

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
