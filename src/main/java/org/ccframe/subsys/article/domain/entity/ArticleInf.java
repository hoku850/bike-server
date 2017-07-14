package org.ccframe.subsys.article.domain.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.ccframe.client.commons.UtilDateTimeClient;
import org.ccframe.commons.cache.AutoCacheConfig;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

@Entity
@Table(name = "PRD_ARTICLE_INF")
//elasticsearch
@Document(indexName = "default_index", type = "articleInf")
@Setting(settingPath = "elasticsearch-analyser.json")
@AutoCacheConfig
public class ArticleInf implements Serializable{

	public static final String ARTICLE_INF_ID = "articleInfId";
	public static final String ARTICLE_TITLE = "articleTitle";
	public static final String ARTICLE_AUTHOR = "articleAuthor";
	public static final String ARTICLE_SOURCE = "articleSource";
	public static final String ARTICLE_CATEGORY_ID = "articleCategoryId";
	public static final String RELEASE_USER_ID = "releaseUserId";
	public static final String RELEASE_TIME = "releaseTime";
	public static final String ARTICLE_CONT = "articleCont";
	public static final String ARTICLE_BRIEF = "articleBrief";
	public static final String APPROVE_STAT_CODE = "approveStatCode";
	public static final String DENY_REASON = "denyReason";
	public static final String ARTICLE_TAG = "articleTag";
	public static final String EXTERNAL_LINK = "externalLink";
	public static final String BROWSE_TIMES = "browseTimes";
	public static final String ARTICLE_POSITION = "articlePosition";
	public static final String META_TITLE = "metaTitle";
	public static final String META_KEYWORDS = "metaKeywords";
	public static final String META_DESCR = "metaDescr";	

	public static final String RELEASE_TIME_STR = "releaseTimeStr";
	public static final String ARTICLE_CONT_STR = "articleContStr";

	private static final long serialVersionUID = 6971226453296699368L;

	public ArticleInf(){}
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //hibernate 5 的 MYSQL 下 AUTO 策略无法对应自增，等修复
    @Column(name = "ARTICLE_INF_ID", nullable = false, length = 10)
	//elasticsearch
	@org.springframework.data.annotation.Id
	private Integer articleInfId;

    @Column(name = "ARTICLE_TITLE", nullable = false, length = 64)
	@Field(type = FieldType.String, analyzer="ngram_analyzer")//使用ngram进行单字分词
	private String articleTitle;

    @Column(name = "ARTICLE_AUTHOR", nullable = true, length = 24)
	private String articleAuthor;

    @Column(name = "ARTICLE_SOURCE", nullable = true, length = 128)
	private String articleSource;

    @Column(name = "ARTICLE_CATEGORY_ID", nullable = true, length = 10)
	private Integer articleCategoryId;

    @Column(name = "RELEASE_USER_ID", nullable = true, length = 10)
	private Integer releaseUserId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RELEASE_TIME", nullable = true, length = 0)
	//elasticsearch
    @Field(type = FieldType.Date, store = true, format = DateFormat.custom, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	private Date releaseTime;

    @Column(name = "ARTICLE_BRIEF", nullable = true, length = 255)
	private String articleBrief;
	
	@Lob
	@Column(name = "ARTICLE_CONT", nullable = false, length = 16777215)
//	 @Field(type = FieldType.String, store = false, analyzer="ngram_analyzer")//使用ngram进行单字分词
	private byte[] articleCont;
	
    @Column(name = "APPROVE_STAT_CODE", nullable = true, length = 2)
	private String approveStatCode;

    @Column(name = "DENY_REASON", nullable = true, length = 255)
	private String denyReason;
	
    @Column(name = "ARTICLE_TAG", nullable = true, length = 255)
    @Field(type = FieldType.String, analyzer="ik") //使用ik分词器进行分词
	private String articleTag;

    @Column(name = "EXTERNAL_LINK", nullable = true, length = 255)
	private String externalLink;

    @Column(name = "BROWSE_TIMES", nullable = true, length = 10)
	private Integer browseTimes;

    @Column(name = "ARTICLE_POSITION", nullable = false, length = 10)
	private Integer articlePosition;

    @Column(name = "META_TITLE", nullable = true, length = 128)
	private String metaTitle;

    @Column(name = "META_KEYWORDS", nullable = true, length = 255)
	private String metaKeywords;
	
    @Column(name = "META_DESCR", nullable = true, length = 255)
	private String metaDescr;

	public Integer getArticleInfId() {
		return articleInfId;
	}

	
	public void setArticleInfId(Integer articleInfId) {
		this.articleInfId = articleInfId;
	}

	
	public String getArticleTitle() {
		return articleTitle;
	}

	
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	
	public String getArticleAuthor() {
		return articleAuthor;
	}

	
	public void setArticleAuthor(String articleAuthor) {
		this.articleAuthor = articleAuthor;
	}

	
	public String getArticleSource() {
		return articleSource;
	}

	
	public void setArticleSource(String articleSource) {
		this.articleSource = articleSource;
	}

	
	public Integer getArticleCategoryId() {
		return articleCategoryId;
	}

	
	public void setArticleCategoryId(Integer articleCategoryId) {
		this.articleCategoryId = articleCategoryId;
	}

	public Integer getReleaseUserId() {
		return releaseUserId;
	}
	
	public void setReleaseUserId(Integer releaseUserId) {
		this.releaseUserId = releaseUserId;
	}

	@JsonIgnore
	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getArticleBrief() {
		return articleBrief;
	}
	
	public void setArticleBrief(String articleBrief) {
		this.articleBrief = articleBrief;
	}

    @JsonIgnore
	public byte[] getArticleCont() {
		return articleCont;
	}

	public void setArticleCont(byte[] articleCont) {
		this.articleCont = articleCont;
	}

	public String getApproveStatCode() {
		return approveStatCode;
	}

	public void setApproveStatCode(String approveStatCode) {
		this.approveStatCode = approveStatCode;
	}

	public String getDenyReason() {
		return denyReason;
	}

	public void setDenyReason(String denyReason) {
		this.denyReason = denyReason;
	}

	public String getArticleTag() {
		return articleTag;
	}

	public void setArticleTag(String articleTag) {
		this.articleTag = articleTag;
	}

	public String getExternalLink() {
		return externalLink;
	}

	public void setExternalLink(String externalLink) {
		this.externalLink = externalLink;
	}

	public Integer getBrowseTimes() {
		return browseTimes;
	}

	public void setBrowseTimes(Integer browseTimes) {
		this.browseTimes = browseTimes;
	}

	public Integer getArticlePosition() {
		return articlePosition;
	}

	public void setArticlePosition(Integer articlePosition) {
		this.articlePosition = articlePosition;
	}

	public String getMetaTitle() {
		return metaTitle;
	}

	public void setMetaTitle(String metaTitle) {
		this.metaTitle = metaTitle;
	}

	public String getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public String getMetaDescr() {
		return metaDescr;
	}

	public void setMetaDescr(String metaDescr) {
		this.metaDescr = metaDescr;
	}

    @Field(type = FieldType.String, analyzer="ik") //使用ik分词器进行分词，只需要做个申明即可让spring-data扫描，无其它意义
	private String articleContStr;
	
	public String getArticleContStr() {
        try {
            return this.articleCont == null ? "" : new String(this.articleCont, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
	}

	public void setArticleContStr(String articleContStr) {
        if(articleContStr == null){
            this.articleCont = null;
        }else{
            try {
                this.articleCont = articleContStr.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
	}

	public String getReleaseTimeStr(){
		return this.getReleaseTime() == null ? null : UtilDateTimeClient.convertDateTimeToString(this.getReleaseTime());
	}
	
	public void setReleaseTimeStr(String timeStr){
		if(timeStr != null){
			this.setReleaseTime(UtilDateTimeClient.convertStringToDateTime(timeStr));
		}
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
		.append(getArticleInfId())
		.toHashCode();
	}

	
	public boolean equals(Object obj) {
		if(!(obj instanceof ArticleInf)){
			return false;
		}
		if(this == obj){
			return true;
		}
		ArticleInf other = (ArticleInf)obj;
		return new EqualsBuilder()
		.append(getArticleInfId(),other.getArticleInfId())
		.isEquals();
	}

	//楚钦2016-07-29添加的
	@Override
	public String toString() {
		return "ArticleInf [articleInfId=" + articleInfId + ", articleTitle="
				+ articleTitle + ", articleAuthor=" + articleAuthor
				+ ", articleSource=" + articleSource + ", articleCategoryId="
				+ articleCategoryId + ", releaseUserId=" + releaseUserId
				+ ", releaseTime=" + releaseTime + ", articleBrief="
				+ articleBrief + ", articleCont="
				+ Arrays.toString(articleCont) + ", approveStatCode="
				+ approveStatCode + ", denyReason=" + denyReason
				+ ", articleTag=" + articleTag + ", externalLink="
				+ externalLink + ", browseTimes=" + browseTimes
				+ ", articlePosition=" + articlePosition + ", metaTitle="
				+ metaTitle + ", metaKeywords=" + metaKeywords + ", metaDescr="
				+ metaDescr + "]";
	}

	
}



