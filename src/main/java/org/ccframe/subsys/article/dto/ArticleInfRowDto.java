package org.ccframe.subsys.article.dto;

import java.util.Date;

import org.ccframe.client.commons.UtilDateTimeClient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 文章列表，只需要列出的几个关键信息.
 * 避免下传太多的冗余数据
 * 
 * 用于前台文章列表和后台文章管理列表.
 * 
 * @author Jim
 *
 */
public class ArticleInfRowDto{
	
	private Integer articleInfId;
	private String articleTitle;
	private String articleAuthor;
	private Date releaseTime;
	private String approveStatCode;
	private String articleBrief;
	private Integer browseTimes;
	private Integer articlePosition;
	private String articleCategoryPath;
	private Integer releaseUserId;
	private String releaseUserNm;

	@JsonIgnore
	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getReleaseTimeStr() {
		return this.getReleaseTime() == null ? null : UtilDateTimeClient.convertDateTimeToMmString(this.getReleaseTime());
	}

	public void setReleaseTimeStr(String timeStr){
		if(timeStr != null){
			this.setReleaseTime(UtilDateTimeClient.convertStringToDateTime(timeStr));
		}
	}
	
	public String getReleaseTimeYYYY_MM_DDStr() {
		return this.getReleaseTime() == null ? null : UtilDateTimeClient.convertDateToString(this.getReleaseTime());
	}

	public Integer getBrowseTimes() {
		return browseTimes;
	}

	public void setBrowseTimes(Integer browseTimes) {
		this.browseTimes = browseTimes;
	}


	public String getArticleCategoryPath() {
		return articleCategoryPath;
	}


	public void setArticleCategoryPath(String articleCategoryPath) {
		this.articleCategoryPath = articleCategoryPath;
	}


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


	public String getApproveStatCode() {
		return approveStatCode;
	}


	public void setApproveStatCode(String approveStatCode) {
		this.approveStatCode = approveStatCode;
	}


	public String getArticleBrief() {
		return articleBrief;
	}


	public void setArticleBrief(String articleBrief) {
		this.articleBrief = articleBrief;
	}


	public Integer getArticlePosition() {
		return articlePosition;
	}


	public void setArticlePosition(Integer articlePosition) {
		this.articlePosition = articlePosition;
	}

//------------------------ extend -----------------------

	public Integer getReleaseUserId() {
		return releaseUserId;
	}

	public void setReleaseUserId(Integer releaseUserId) {
		this.releaseUserId = releaseUserId;
	}

	public String getReleaseUserNm() {
		return releaseUserNm;
	}

	public void setReleaseUserNm(String releaseUserNm) {
		this.releaseUserNm = releaseUserNm;
	}

	public boolean getIsWeekNew() {
		if(releaseTime == null){
			return false;
		}
		return new Date().getTime() - releaseTime.getTime() < 604800000L;
	}
}
