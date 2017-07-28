package org.ccframe.subsys.bike.dto;

import java.util.List;

import org.ccframe.client.commons.ClientPage;


public class CyclingOrderClientPage<E> extends ClientPage<E>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 386442083002776320L;

	public CyclingOrderClientPage() {
		super();
	}

	public CyclingOrderClientPage(int totalLength, int page, int size, List<E> list) {
		super(totalLength, page, size, list);
	}

	private Double cyclingOrderTotalAmount;

	public Double getCyclingOrderTotalAmount() {
		return cyclingOrderTotalAmount;
	}

	public void setCyclingOrderTotalAmount(Double cyclingOrderTotalAmount) {
		this.cyclingOrderTotalAmount = cyclingOrderTotalAmount;
	}
}