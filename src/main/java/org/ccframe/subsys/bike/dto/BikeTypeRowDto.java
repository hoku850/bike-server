package org.ccframe.subsys.bike.dto;

import org.ccframe.subsys.bike.domain.entity.BikeType;

public class BikeTypeRowDto extends BikeType {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4861275632649987628L;
	
	private String orgNm;

	public String getOrgNm() {
		return orgNm;
	}

	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	
}
