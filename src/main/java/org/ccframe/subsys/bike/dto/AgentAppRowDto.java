package org.ccframe.subsys.bike.dto;

import org.ccframe.subsys.bike.domain.entity.AgentApp;

public class AgentAppRowDto extends AgentApp{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String orgNm;

	public String getOrgNm() {
		return orgNm;
	}

	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	
	
}
