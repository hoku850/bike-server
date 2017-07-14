package org.ccframe.subsys.article.service;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.ccframe.client.Global;
import org.ccframe.client.ResGlobal;
import org.ccframe.client.commons.AdminUser;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.BusinessException;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.article.domain.entity.ArticleInf;
import org.ccframe.subsys.article.dto.ArticleInfDto;
import org.ccframe.subsys.article.repository.ArticleInfRepository;
import org.ccframe.subsys.core.domain.code.BusinessTypeCodeEnum;
import org.ccframe.subsys.core.service.FileInfService;
import org.ccframe.subsys.core.service.ITreeNodeDeleteCheck;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleInfService extends BaseService<ArticleInf, Integer, ArticleInfRepository> implements ITreeNodeDeleteCheck{

//	/**
//	 * 已发布的头N条消息.API使用
//	 * @param articleCategoryId
//	 * @param topN
//	 * @return
//	 */
//	@Transactional(readOnly = true)
//	public List<ArticleInfRowDto> findArticleInfByArticleCategoryId(Integer articleCategoryId, Integer topN){
//		ArticleInfListReq articleInfListReq = new ArticleInfListReq();
//		articleInfListReq.setArticleCategoryId(articleCategoryId);
//		articleInfListReq.setApproveStatCode(ApproveStatCodeEnum.APPROVE.toCode());
//		return findArticleInfList(articleInfListReq, 0, topN).getList();
//	}

//	/**
//	 * 查找某个分类下的文章列表.
//	 * @param articleCategoryId
//	 * @param topN
//	 * @return
//	 */
//	@Transactional(readOnly = true)
//	public List<ArticleInf> findArticleInfListByArticleCategoryIdTopN(Integer articleCategoryId, Integer topN){
//		try{
//			QueryBuilder<ArticleInf, Integer> builder = this.getRepository().queryBuilder();
//			builder.offset(0L);
//			builder.limit(topN.longValue());
//			builder.orderBy(ArticleInf.ARTICLE_POSITION_FIELD, true).orderBy(ArticleInf.RELEASE_TIME_FIELD, false);
//			builder.where().in(ArticleInf.ARTICLE_CATEGORY_ID_FIELD, SpringContextHelper.getBean(TreeNodeService.class).getTreeNodeIdList(articleCategoryId));
//			return builder.query();
//		}catch(SQLException e){
//			throw new BusinessException(e.getMessage(), e);
//		}
//		//TODO FIX IT
//		return this.getRepository().findAll();
//	}
	
	
//	/**
//	 * 分页查找文章列表.
//	 * @param articleInfListReq
//	 * @param page 页号，从0开始
//	 * @param size 页长
//	 * @return
//	 */
//	@Transactional(readOnly = true)
//	@Deprecated //使用索引来查询
//	public ClientPage<ArticleInfRowDto> findArticleInfList(ArticleInfListReq articleInfListReq, Integer page, Integer size){
//		Page<ArticleInf> articleInfPage = this.getRepository().findAll(
//			new Criteria<ArticleInf>()
//			.add(Restrictions.in(ArticleInf.ARTICLE_CATEGORY_ID, SpringContextHelper.getBean(TreeNodeService.class).getTreeNodeIdList(articleInfListReq.getArticleCategoryId(), null)))
//			.add(Restrictions.like(ArticleInf.ARTICLE_TITLE, articleInfListReq.getArticleTitle()))
//			.add(Restrictions.like(ArticleInf.ARTICLE_TAG, articleInfListReq.getArticleTag()))
//			.add(Restrictions.like(ArticleInf.APPROVE_STAT_CODE, articleInfListReq.getApproveStatCode())),
//			new PageRequest(page, size, new Sort(
//				new Order(Direction.ASC, ArticleInf.ARTICLE_POSITION), new Order(Direction.DESC, ArticleInf.RELEASE_TIME), new Order(Direction.ASC, ArticleInf.ARTICLE_INF_ID))
//			)
//		);
//		List<ArticleInfRowDto> resultList = new ArrayList<ArticleInfRowDto>();
//		for(ArticleInf articleInf:articleInfPage.getContent()){
//			ArticleInfRowDto rowRecord = new ArticleInfRowDto();
//			BeanUtils.copyProperties(articleInf, rowRecord);
//			rowRecord.setReleaseUserNm(SpringContextHelper.getBean(UserService.class).getById(articleInf.getReleaseUserId()).getUserNm());
//			rowRecord.setArticleCategoryPath(StringUtils.join(
//				SpringContextHelper.getBean(TreeNodeService.class).getNamePath(articleInf.getArticleCategoryId(), Global.TreeRootEnum.ARTICLE_CATEGORY_TREE_ROOT.getTreeNodeId()),
//				">"
//			));
//			resultList.add(rowRecord);
//		}
//		return new ClientPage<ArticleInfRowDto>((int)articleInfPage.getTotalElements(), articleInfPage.getNumber(), size, resultList);
//	}

	@Override
	public void checkBusiness(Integer treeNodeId) {
		if(! findByKey(ArticleInf.ARTICLE_CATEGORY_ID, treeNodeId).isEmpty()){
			throw new BusinessException(ResGlobal.ERRORS_USER_DEFINED, new String[]{"请先删除分类下文章再删除分类节点！"});
		}
	}

	@Transactional
	public ArticleInf saveOrUpdateArticleInfDto(ArticleInfDto articleInfDto) { //TODO APPROVE CODE 还是错的
		ArticleInf articleInf = new ArticleInf();
		BeanUtils.copyProperties(articleInfDto, articleInf);
		articleInf.setReleaseTime(new Date());
		articleInf.setReleaseUserId(((AdminUser)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_ADMIN)).getUserId());
		saveAndFlush(articleInf);
		Map<String, String> replaceMap = SpringContextHelper.getBean(FileInfService.class).syncFileInfList(BusinessTypeCodeEnum.ARTICLE, articleInfDto.getArticleInfId(), articleInfDto.getFileInfBarDtoList());
		String articleContStr = articleInfDto.getArticleContStr();
		for(Entry<String,String> entry: replaceMap.entrySet()){
			articleContStr = StringUtils.replace(articleContStr, entry.getKey(), entry.getValue());
		}
		articleInf.setArticleContStr(articleContStr);
		articleInf.setArticleBrief(articleContStr.length() > 255 ? articleInfDto.getArticleContStr().substring(0, 255) : articleContStr);
//		if(1<2)throw new NullPointerException();
		//TODO meta 的保存
		save(articleInf); //更新地址里的temp url
		return articleInf;
	}

//	public List<ArticleInfIdProjection> findAllIdProjection(){
		
//		JPAQuery query = new JpaQueryCreator().createQuery().
//				QProject q = new QProject();
//		return this.getRepository().
//				
//				TypedQuery <Object[]> query = em.createQuery(
//						  "SELECT p.projectId, p.projectName FROM projects AS p", Object[].class);
//
//						List<Object[]> results = query.getResultList();
//						or you can use native sql query.
//
//						Query query = em.createNativeQuery("sql statement");
//						List<Object[]> results = query.getResultList();
				
//	}
	
//	public ArticleInfDto getArticleInfDto(Integer articleInfId) {
//		ArticleInfDto articleInfDto = new ArticleInfDto();
//		articleInfDto.setArticleInf(this.getById(articleInfId));
//		articleInfDto.setSysFileInfList(
//			SpringContextHelper.getBean(SysFileInfService.class).findByBusinessTypeCodeBusinessObjectId(
//				BusinessTypeCodeEnum.ARTICLE,
//				articleInfDto.getArticleInf().getArticleInfId(),
//				false
//			)
//		);
//		return articleInfDto;
//	}

//	public Page<ArticleInfRowDto> findArticleInfPageByArticleCategoryId(Integer articleCategoryId, Integer pageSize, Integer pageNum) {
//		ArticleInfListReq articleInfListReq = new ArticleInfListReq();
//		articleInfListReq.setArticleCategoryId(articleCategoryId);
//		articleInfListReq.setApproveStatCode(ApproveStatCodeEnum.APPROVE.toCode());
//		int total = (int)findArticleInfTotal(articleInfListReq, pageSize, pageNum);
//		Page<ArticleInfRowDto> result = new Page<ArticleInfRowDto>(10,0, findArticleInfList(articleInfListReq, 1, 10));
//		return result;
//	}
}
