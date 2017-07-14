package org.ccframe.subsys.core.dto;

/**
 * 系统文件列表请求条件
 * @author Jim
 *
 */
public class FileInfListReq{
	private String businessTypeCode;
	private String ifFailUse;
	private String fileNm;
	private Integer businessObjectId;
	public String getBusinessTypeCode() {
		return businessTypeCode;
	}
	public void setBusinessTypeCode(String businessTypeCode) {
		this.businessTypeCode = businessTypeCode;
	}
	public String getIfFailUse() {
		return ifFailUse;
	}
	public void setIfFailUse(String ifFailUse) {
		this.ifFailUse = ifFailUse;
	}
	public String getFileNm() {
		return fileNm;
	}
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}
	public Integer getBusinessObjectId() {
		return businessObjectId;
	}
	public void setBusinessObjectId(Integer businessObjectId) {
		this.businessObjectId = businessObjectId;
	}
}
