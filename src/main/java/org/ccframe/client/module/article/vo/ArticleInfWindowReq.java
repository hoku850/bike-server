package org.ccframe.client.module.article.vo;

public class ArticleInfWindowReq {
	
	private Integer articleInfId;
	
	private Integer articleCategoryId;

	public ArticleInfWindowReq(){}
	
	public ArticleInfWindowReq(Integer articleInfId, Integer articleCategoryId){
		this.articleInfId = articleInfId;
		this.articleCategoryId = articleCategoryId;
	}
	
	public Integer getArticleInfId() {
		return articleInfId;
	}

	public void setArticleInfId(Integer articleInfId) {
		this.articleInfId = articleInfId;
	}

	public Integer getArticleCategoryId() {
		return articleCategoryId;
	}

	public void setArticleCategoryId(Integer articleCategoryId) {
		this.articleCategoryId = articleCategoryId;
	}
}

