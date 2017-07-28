package org.ccframe.subsys.article.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.article.domain.entity.ArticleInf;
import org.ccframe.subsys.article.dto.ArticleInfListReq;
import org.ccframe.subsys.article.dto.ArticleInfRowDto;
import org.ccframe.subsys.article.search.ArticleInfSearchRepository;
import org.ccframe.subsys.core.domain.code.ApproveStatCodeEnum;
import org.ccframe.subsys.core.service.TreeNodeSearchService;
import org.ccframe.subsys.core.service.TreeNodeService;
import org.ccframe.subsys.core.service.UserService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleInfSearchService extends BaseSearchService<ArticleInf, Integer, ArticleInfSearchRepository>{
	
	/**
	 * 已发布的头N条消息.API使用
	 * @param articleCategoryId
	 * @param topN
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<ArticleInfRowDto> findArticleInfByArticleCategoryIdTopN(Integer articleCategoryId, Integer topN){
		ArticleInfListReq articleInfListReq = new ArticleInfListReq();
		articleInfListReq.setArticleCategoryId(articleCategoryId);
		articleInfListReq.setApproveStatCode(ApproveStatCodeEnum.APPROVE.toCode());
		return findArticleInfList(articleInfListReq, 0, topN).getList();
	}

	/**
	 * 查找某个分类（不含子类）下的文章列表.
	 * @param articleCategoryId
	 * @param topN
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<ArticleInf> findArticleInfListByArticleCategoryIdTopN(Integer articleCategoryId, Integer topN){
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(QueryBuilders.termQuery(ArticleInf.ARTICLE_CATEGORY_ID, articleCategoryId));
		Page<ArticleInf> articleInfPage = this.getRepository().search(
			boolQueryBuilder,
			new PageRequest(0, topN, new Sort( //如果不搜索则默认按照时间倒序、ID正序
				new Order(Direction.ASC, ArticleInf.ARTICLE_POSITION), new Order(Direction.DESC, ArticleInf.RELEASE_TIME), new Order(Direction.ASC, ArticleInf.ARTICLE_INF_ID))
			)
		);
		return articleInfPage.getContent();
	}

	/**
	 * 分页查找文章列表.
	 * @param articleInfListReq
	 * @param page 页号，从0开始
	 * @param size 页长
	 * @return
	 */
	public ClientPage<ArticleInfRowDto> findArticleInfList(ArticleInfListReq articleInfListReq, Integer page, Integer size){
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		List<Integer> treeNodeIdList = SpringContextHelper.getBean(TreeNodeSearchService.class).getTreeNodeIdList(articleInfListReq.getArticleCategoryId(), null);
		if(CollectionUtils.isNotEmpty(treeNodeIdList)){
			boolQueryBuilder.must(QueryBuilders.termsQuery(ArticleInf.ARTICLE_CATEGORY_ID, treeNodeIdList));
		}
		if(StringUtils.isNotBlank(articleInfListReq.getArticleSearch())){
			//综合搜索，搜索标题、作者、标签及内容
			boolQueryBuilder.should(QueryBuilders.matchQuery(ArticleInf.ARTICLE_TITLE, articleInfListReq.getArticleSearch()).operator(Operator.AND).boost(100)); //标题like匹配
			boolQueryBuilder.should(QueryBuilders.matchQuery(ArticleInf.ARTICLE_AUTHOR, articleInfListReq.getArticleSearch()).boost(100)); //作者必须完全匹配
			boolQueryBuilder.should(QueryBuilders.matchQuery(ArticleInf.ARTICLE_TAG, articleInfListReq.getArticleSearch()).operator(Operator.AND).boost(100)); //标签分词匹配
			//内容分词匹配，但是优先级最低，boost默认
			boolQueryBuilder.should(QueryBuilders.matchQuery(ArticleInf.ARTICLE_CONT_STR, articleInfListReq.getArticleSearch()).operator(Operator.AND));
		}
//		if(StringUtils.isNotBlank(articleInfListReq.getArticleTitle())){
//			boolQueryBuilder.must(QueryBuilders.fuzzyQuery(ArticleInf.ARTICLE_TITLE, articleInfListReq.getArticleTitle()));
//			MatchQueryBuilder builder = QueryBuilders.matchQuery(ArticleInf.ARTICLE_TITLE, articleInfListReq.getArticleTitle());
//			builder.operator(Operator.AND); //默认是or条件，需要设置and条件提高精度
//			builder.type(Type.PHRASE); //再提高精度，完全词匹配（如果是基于词法的精确分析要设置，如果是基于ngram的分析则不能设置还要配合Operator.AND）
//			boolQueryBuilder.must(builder);
//		}
		if(StringUtils.isNotBlank(articleInfListReq.getApproveStatCode())){
			boolQueryBuilder.must(QueryBuilders.fuzzyQuery(ArticleInf.APPROVE_STAT_CODE, articleInfListReq.getApproveStatCode()));
		}
		Page<ArticleInf> articleInfPage = this.getRepository().search(
			boolQueryBuilder,
			StringUtils.isBlank(articleInfListReq.getArticleSearch()) ?
			new PageRequest(page, size, new Sort( //如果不搜索则默认按照时间倒序、ID正序
				new Order(Direction.ASC, ArticleInf.ARTICLE_POSITION), new Order(Direction.DESC, ArticleInf.RELEASE_TIME), new Order(Direction.ASC, ArticleInf.ARTICLE_INF_ID))
			) :
			new PageRequest(page, size) //如果搜索则根据根据boost文档相关度排序
		);
		List<ArticleInfRowDto> resultList = new ArrayList<ArticleInfRowDto>();
		for(ArticleInf articleInf:articleInfPage.getContent()){
			ArticleInfRowDto rowRecord = new ArticleInfRowDto();
			BeanUtils.copyProperties(articleInf, rowRecord);
			rowRecord.setReleaseUserNm(SpringContextHelper.getBean(UserService.class).getById(articleInf.getReleaseUserId()).getUserNm());
			rowRecord.setArticleCategoryPath(StringUtils.join(
				SpringContextHelper.getBean(TreeNodeService.class).getNamePath(articleInf.getArticleCategoryId(), Global.TreeRootEnum.ARTICLE_CATEGORY_TREE_ROOT.getTreeNodeId()),
				">"
			));
			resultList.add(rowRecord);
		}
		return new ClientPage<ArticleInfRowDto>((int)articleInfPage.getTotalElements(), articleInfPage.getNumber(), size, resultList);
	}

}
