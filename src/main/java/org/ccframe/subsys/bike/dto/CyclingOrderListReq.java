package org.ccframe.subsys.bike.dto;

import java.util.Date;

import org.ccframe.client.Global;
import org.ccframe.client.commons.UtilDateTimeClient;

/**
 * 骑行订单请求参数
 * 
 * @author 梁期智
 *
 */
public class CyclingOrderListReq {

	private Integer orgId; // 运营商ID
	private Integer bikeTypeId; // 骑行
	private String orderState;

	private Date startTime;
	private Date endTime;

	private String searchField;

	@Override
	public String toString() {
		return "CyclingOrderListReq [orgId=" + orgId + ", bikeTypeId="
				+ bikeTypeId + ", orderState=" + orderState + ", startTime="
				+ startTime + ", endTime=" + endTime + ", searchField="
				+ searchField + "]";
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getBikeTypeId() {
		return bikeTypeId;
	}

	public void setBikeTypeId(Integer bikeTypeId) {
		this.bikeTypeId = bikeTypeId;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	// 增强型方法satat
	public String getStartTimeStr() {
		return this.getStartTime() == null ? null : UtilDateTimeClient
				.convertDateTimeToString(this.getStartTime());
	}

	public void setStartTimeStr(String value) {
		if (value != null) {
			this.setStartTime(UtilDateTimeClient.convertStringToDateTime(value));
		}
	}

	public String getEndTimeStr() {
		return this.getEndTime() == null ? null : UtilDateTimeClient
				.convertDateTimeToString(this.getEndTime());
	}

	public void setEndTimeStr(String value) {
		value = value.replace(Global.DAY_START_TIME, Global.DAY_END_TIME);
		if (value != null) {
			this.setEndTime(UtilDateTimeClient.convertStringToDateTime(value));
		}
	}
	// 增强型方法end
}
