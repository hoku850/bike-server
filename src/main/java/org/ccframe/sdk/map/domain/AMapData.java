package org.ccframe.sdk.map.domain;

public class AMapData {
	
	private String centerPosition; // 定位点
	private String startPosition;
	private String endPosition;
	private Integer zoom; // 缩放
	private String lineArr; // 轨迹

	private String icon; // 标记点的图标
	private String iconPosition; // 标记点的位置

	public String getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(String startPosition) {
		this.startPosition = startPosition;
	}

	public String getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(String endPosition) {
		this.endPosition = endPosition;
	}

	public String getCenterPosition() {
		return centerPosition;
	}

	public void setCenterPosition(String centerPosition) {
		this.centerPosition = centerPosition;
	}

	public Integer getZoom() {
		return zoom;
	}

	public void setZoom(Integer zoom) {
		this.zoom = zoom;
	}

	public String getLineArr() {
		return lineArr;
	}

	public void setLineArr(String lineArr) {
		this.lineArr = lineArr;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconPosition() {
		return iconPosition;
	}

	public void setIconPosition(String iconPosition) {
		this.iconPosition = iconPosition;
	}

}
