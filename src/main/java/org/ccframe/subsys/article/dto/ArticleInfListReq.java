package org.ccframe.subsys.article.dto;

/**
 * 文章列表请求条件
 * @author Jim
 *
 */
public class ArticleInfListReq{
	private Integer articleCategoryId;
	private String articleSearch;
	private String approveStatCode;
	
	public Integer getArticleCategoryId() {
		return articleCategoryId;
	}
	public void setArticleCategoryId(Integer articleCategoryId) {
		this.articleCategoryId = articleCategoryId;
	}
	public String getArticleSearch() {
		return articleSearch;
	}
	public void setArticleSearch(String articleSearch) {
		this.articleSearch = articleSearch;
	}
	public String getApproveStatCode() {
		return approveStatCode;
	}
	public void setApproveStatCode(String approveStatCode) {
		this.approveStatCode = approveStatCode;
	}
}
