package org.ccframe.subsys.core.domain.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.ccframe.commons.cache.AutoCacheConfig;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;


@Entity
@Table(name = "SYS_PARAM")
@AutoCacheConfig
public class Param implements Serializable{

	private static final long serialVersionUID = -8285169606075608779L;

	public static final String PARAM_ID = "paramId";
	public static final String PARAM_NM = "paramNm";
	public static final String PARAM_INNER_CODING = "paramInnerCoding";
	public static final String PARAM_DESCR = "paramDescr";
	public static final String PARAM_TYPE_CODE = "paramTypeCode";
	public static final String PARAM_SELECT_ITEM = "paramSelectItem";
	public static final String PARAM_VALUE = "paramValue";

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
	@Column(name = "PARAM_ID", nullable = false, length = 10)
	private Integer paramId;

    @Column(name = "PARAM_NM", nullable = false, length = 24)
	private String paramNm;

    @Column(name = "PARAM_INNER_CODING", nullable = false, length = 24)
	private String paramInnerCoding;

    @Column(name = "PARAM_DESCR", nullable = true, length = 255)
	private String paramDescr;

    @Column(name = "PARAM_TYPE_CODE", nullable = true, length = 2)
	private String paramTypeCode;
    
    @Column(name = "PARAM_SELECT_ITEM", nullable = true, length = 255)
	private String paramSelectItem;
    
	@Lob
	@Column(name = "PARAM_VALUE", nullable = false, length = 16777215)
	private byte[] paramValue;

	public Integer getParamId() {
		return paramId;
	}

	public void setParamId(Integer paramId) {
		this.paramId = paramId;
	}

	public String getParamNm() {
		return paramNm;
	}

	public void setParamNm(String paramNm) {
		this.paramNm = paramNm;
	}

	public String getParamInnerCoding() {
		return paramInnerCoding;
	}

	public void setParamInnerCoding(String paramInnerCoding) {
		this.paramInnerCoding = paramInnerCoding;
	}

	public String getParamDescr() {
		return paramDescr;
	}

	public void setParamDescr(String paramDescr) {
		this.paramDescr = paramDescr;
	}

	public String getParamSelectItem() {
		return paramSelectItem;
	}

	public void setParamSelectItem(String paramSelectItem) {
		this.paramSelectItem = paramSelectItem;
	}

    @JsonIgnore
	public byte[] getParamValue() {
		return paramValue;
	}

	public void setParamValue(byte[] paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamTypeCode() {
		return paramTypeCode;
	}

	public void setParamTypeCode(String paramTypeCode) {
		this.paramTypeCode = paramTypeCode;
	}

	public String getParamValueStr() {
        try {
            return this.paramValue == null ? "" : new String(this.paramValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
	}

	public void setParamValueStr(String paramValueStr) {
        if(paramValueStr == null){
            this.paramValue = null;
        }else{
            try {
                this.paramValue = paramValueStr.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Param)){
			return false;
		}
		if(this == obj){
			return true;
		}
		Param other = (Param)obj;
		return new EqualsBuilder()
		.append(getParamId(),other.getParamId())
		.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
		.append(getParamId())
		.toHashCode();
	}

}


