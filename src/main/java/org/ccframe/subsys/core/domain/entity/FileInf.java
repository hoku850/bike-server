package org.ccframe.subsys.core.domain.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.ccframe.client.commons.UtilDateTimeClient;
import org.ccframe.commons.cache.AutoCacheConfig;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

@Entity
@Table(name = "SYS_FILE_INF")
@AutoCacheConfig
public class FileInf implements Serializable{

	private static final long serialVersionUID = 6971226453296699368L;

	public static final String FILE_INF_ID = "fileInfId";
	public static final String FILE_PATH = "filePath";
	public static final String BUSINESS_TYPE_CODE = "businessTypeCode";
	public static final String BUSINESS_OBJECT_ID = "businessObjectId";
	public static final String FILE_NM = "fileNm";
	public static final String FILE_TYPE_NM = "fileTypeNm";
	public static final String FILE_TIME = "fileTime";
	public static final String IF_FAIL_USE = "ifFailUse";

	public static final String FILE_INF_ID_FIELD = "FILE_INF_ID";
	public static final String FILE_PATH_FIELD = "FILE_PATH";
	public static final String BUSINESS_TYPE_CODE_FIELD = "BUSINESS_TYPE_CODE";
	public static final String BUSINESS_OBJECT_ID_FIELD = "BUSINESS_OBJECT_ID";
	public static final String FILE_NM_FIELD = "FILE_NM";
	public static final String FILE_TYPE_NM_FIELD = "FILE_TYPE_NM";
	public static final String FILE_TIME_FIELD = "FILE_TIME";
	public static final String IF_FAIL_USE_FIELD = "IF_FAIL_USE";
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
    @Column(name = FILE_INF_ID_FIELD, nullable = false, length = 10)
	private Integer fileInfId;
	
    @Column(name = BUSINESS_TYPE_CODE_FIELD, nullable = false, length = 2)
	private String businessTypeCode;

    @Column(name = BUSINESS_OBJECT_ID_FIELD, nullable = false, length = 128)
	private Integer businessObjectId;

    @Column(name = FILE_NM_FIELD, nullable = false, length = 60)
	private String fileNm;

    @Column(name = FILE_TYPE_NM_FIELD, nullable = true, length = 4)
	private String fileTypeNm;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = FILE_TIME_FIELD, nullable = false, length = 0)
	private Date fileTime;

    @Column(name = FILE_PATH_FIELD, nullable = false, length = 60)
	private String filePath;

    @Column(name = IF_FAIL_USE_FIELD, nullable = false, length = 2)
	private String ifFailUse;

	public Integer getFileInfId() {
		return fileInfId;
	}

	public void setFileInfId(Integer fileInfId) {
		this.fileInfId = fileInfId;
	}

	public String getBusinessTypeCode() {
		return businessTypeCode;
	}

	public void setBusinessTypeCode(String businessTypeCode) {
		this.businessTypeCode = businessTypeCode;
	}

	public Integer getBusinessObjectId() {
		return businessObjectId;
	}

	public void setBusinessObjectId(Integer businessObjectId) {
		this.businessObjectId = businessObjectId;
	}

	public String getFileNm() {
		return fileNm;
	}

	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}

	public String getFileTypeNm() {
		return fileTypeNm;
	}

	public void setFileTypeNm(String fileTypeNm) {
		this.fileTypeNm = fileTypeNm;
	}

    @JsonIgnore
	public Date getFileTime() {
		return fileTime;
	}

    @JsonIgnore
	public void setFileTime(Date fileTime) {
		this.fileTime = fileTime;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getIfFailUse() {
		return ifFailUse;
	}

	public void setIfFailUse(String ifFailUse) {
		this.ifFailUse = ifFailUse;
	}

	public String getFileTimeStr(){
		return UtilDateTimeClient.convertDateTimeToString(this.getFileTime());
	}
	
	public void setFileTimeStr(String timeStr){
		this.fileTime = UtilDateTimeClient.convertStringToDateTime(timeStr);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof FileInf)){
			return false;
		}
		if(this == obj){
			return true;
		}
		FileInf other = (FileInf)obj;
		return new EqualsBuilder()
		.append(getFileInfId(),other.getFileInfId())
		.isEquals();
	}

	@Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(getFileInfId())
                .toHashCode();
    }

}


