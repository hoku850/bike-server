package org.ccframe.commons.data;

import java.util.Date;

/**
 * 导入进程的显示数据.
 * 更新时修改updateTime，如果任务updateTime超过10分钟，则可以考虑清除
 * @author JIM
 *
 */
public class AsyncImportData {
	private String uuid;
	private double percent;
	private Throwable exception;
	private String errorText;
	private Date updateTime;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}
	public Throwable getException() {
		return exception;
	}
	public void setException(Throwable exception) {
		this.exception = exception;
	}
	public String getErrorText() {
		return errorText;
	}
	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
