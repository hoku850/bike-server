package org.ccframe.sdk.article.function;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.sdk.article.proxy.ArticleInfProxy;
import org.ccframe.subsys.article.domain.entity.ArticleInf;
import org.ccframe.subsys.article.dto.ArticleInfListReq;
import org.ccframe.subsys.article.dto.ArticleInfRowDto;
import org.ccframe.subsys.article.service.ArticleInfSearchService;
import org.ccframe.subsys.article.service.ArticleInfService;
import org.springframework.beans.BeanUtils;

public class ArticleApi {

	
    public static List<ArticleInfProxy> findTest(Integer articleCategoryId, Integer topN) throws BusinessException {
		List<ArticleInf> articleInfList = SpringContextHelper.getBean(ArticleInfSearchService.class).findArticleInfListByArticleCategoryIdTopN(articleCategoryId, topN);
		List<ArticleInfProxy> resultList = new ArrayList<ArticleInfProxy>();
		for(ArticleInf articleInf: articleInfList){
			ArticleInfProxy articleInfProxy = new ArticleInfProxy();
			BeanUtils.copyProperties(articleInf, articleInfProxy);
			resultList.add(articleInfProxy);
		}
		return resultList;
    }
	
    /**
     * 按文章ID取文章内容
     * @param articleId
     * @return
     * @throws BusinessException
     */
    public static ArticleInf getArticleById(Integer articleId) throws BusinessException {
        return SpringContextHelper.getBean(ArticleInfService.class).getById(articleId);
    }

    /**
     * 按分类取文章,返回List<ArticleInfRowDto>
     * @param articleCategoryId
     * @param topN
     * @return ArticleInfRowDto
     * @throws BusinessException
     */
    public static List<ArticleInfRowDto> findArticleInfByArticleCategoryId(Integer articleCategoryId, Integer topN) throws BusinessException {
        return SpringContextHelper.getBean(ArticleInfSearchService.class).findArticleInfByArticleCategoryIdTopN(articleCategoryId, topN);
    }
    
    /**
     * 按标签取文章。从不同文章类型中选择适合的文章。例如"今日头条"，"热点聚焦"
     * @param articleTag
     * @param topN
     * @return
     * @throws BusinessException
     */
//    public static List<ArticleInfRowDto> findArticleInfByArticleTag(String articleTag, Integer topN) throws BusinessException {
//        return SpringContextHelper.getBean(ArticleInfService.class).findArticleInfByArticleTag(articleTag, topN);
//    }

    /**
     * 按分类取文章分页方式
     * @param articleCategoryId
     * @param topN
     * @return
     * @throws BusinessException
     */
    public static ClientPage<ArticleInfRowDto> findArticleInfPageByArticleCategoryId(Integer articleCategoryId, Integer page, Integer size) throws BusinessException {
    	//TODO 返回分页的对象，分页对象通过 WebContextHolder.getSessionContextStore().getRequestMap() 构造请求链接，并替换其中的offset参数
        ArticleInfListReq articleInfListReq = new ArticleInfListReq();
        articleInfListReq.setArticleCategoryId(articleCategoryId);
		return SpringContextHelper.getBean(ArticleInfSearchService.class).findArticleInfList(articleInfListReq, page, size);
    }
    
    /**
     * 通过articleId取出一个ArticleInfProxy，ArticleInfProxy能获取user，还有与它同级的文章列表
     * @param articleId
     * @return
     */
    public static ArticleInfProxy getArticleInfProxyById(Integer articleId) throws BusinessException{
    	ArticleInf articleInf = SpringContextHelper.getBean(ArticleInfService.class).getById(articleId);
    	ArticleInfProxy articleInfProxy = new ArticleInfProxy();
		BeanUtils.copyProperties(articleInf, articleInfProxy);
		return articleInfProxy;
    }

}
