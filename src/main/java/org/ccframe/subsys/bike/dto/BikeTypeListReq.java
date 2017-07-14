package org.ccframe.subsys.bike.dto;

/**
 * 单车类型请求参数
 * @author 梁期智
 *
 */
public class BikeTypeListReq {

	private Integer orgId; // 运营商ID

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
}
