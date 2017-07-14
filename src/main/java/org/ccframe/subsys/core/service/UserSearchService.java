package org.ccframe.subsys.core.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.commons.base.OffsetBasedPageRequest;
import org.ccframe.commons.cache.AutoCacheConfig;
import org.ccframe.commons.cache.CacheEvictBy;
import org.ccframe.subsys.core.domain.code.UserStatCodeEnum;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.dto.UserListReq;
import org.ccframe.subsys.core.dto.UserRowDto;
import org.ccframe.subsys.core.search.UserSearchRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class UserSearchService extends BaseSearchService<User, Integer, UserSearchRepository>{

	public ClientPage<UserRowDto> findUserList(UserListReq userListReq, int offset, int limit) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		if(StringUtils.isNotBlank(userListReq.getLoginId())){
			boolQueryBuilder.must(QueryBuilders.matchQuery(User.LOGIN_ID, userListReq.getLoginId()));
		}
		if(StringUtils.isNotBlank(userListReq.getUserNm())){
			MatchQueryBuilder builder = QueryBuilders.matchQuery(User.USER_NM, userListReq.getUserNm());
			builder.operator(Operator.AND);
			boolQueryBuilder.must(builder);
		}
		if(StringUtils.isNotBlank(userListReq.getUserNm())){
			RangeQueryBuilder builder = QueryBuilders.rangeQuery(User.CREATE_DATE);
			builder.gte(userListReq.getCreateDateEnd() == null ? Global.MIN_SEARCH_DATE : userListReq.getCreateDateEnd());
			builder.lte(userListReq.getCreateDateStart() == null ? Global.MAX_SEARCH_DATE : userListReq.getCreateDateStart());
			boolQueryBuilder.must(builder);
		}
		boolQueryBuilder.must(QueryBuilders.termsQuery(
			User.USER_STAT_CODE, 
			StringUtils.isBlank(userListReq.getUserStatCode()) ? 
			new String[]{UserStatCodeEnum.NORMAL.toCode(), UserStatCodeEnum.FREEZE.toCode()} :
			new String[]{userListReq.getUserStatCode()}
		));
		Page<User> userPage = this.getRepository().search(
			boolQueryBuilder,
			new OffsetBasedPageRequest(offset, limit, new Order(Direction.DESC, User.CREATE_DATE))
		);

		//TODO搜索用solr
//		Page<User> userPage = getRepository().findAll(
//			new Criteria<User>()
//			.add(Restrictions.eq(User.LOGIN_ID, userListReq.getLoginId()))
//			.add(Restrictions.like(User.USER_NM, userListReq.getUserNm()))
//			.add(Restrictions.gt(User.CREATE_DATE, userListReq.getCreateDateStart()))
//			.add(Restrictions.lt(User.CREATE_DATE, userListReq.getCreateDateEnd()))
//			.add(Restrictions.in(
//				User.USER_STAT_CODE, 
//				StringUtils.isBlank(userListReq.getUserStatCode()) ? 
//					new String[]{UserStatCodeEnum.NORMAL.toCode(), UserStatCodeEnum.FREEZE.toCode()} :
//					new String[]{userListReq.getUserStatCode()}
//			)), 
//			new OffsetBasedPageRequest(offset, limit, new Order(Direction.DESC, User.CREATE_DATE))
//		);
//				
		List<UserRowDto> resultList = new ArrayList<UserRowDto>();
		for(User user:userPage.getContent()){
			UserRowDto rowRecord = new UserRowDto();
			BeanUtils.copyProperties(user, rowRecord);
			user.setUserPsw("");//列表返回屏蔽用户密码
			resultList.add(rowRecord);
		}
		return new ClientPage<UserRowDto>((int)userPage.getTotalElements(), offset / limit, limit, resultList);
	}

	/**
	 * 由于用户名必须有字母数字，手机为全数字，email包含@，因此3种规则不会重复，可用于唯一判断。
	 * @param multiLoginId
	 * @param userPsw
	 * @return
	 */
	@Cacheable(value=Global.EH_CACHE_AUTO_CACHE, cacheResolver = Global.EH_CACHE_RESOLVER)
	@CacheEvictBy({User.class})
	@AutoCacheConfig(100)
	public User getByMultiLoginIdUserPsw(String multiLoginId, String userPsw){
		BoolQueryBuilder orQueryBuilder = QueryBuilders.boolQuery();
		orQueryBuilder.should(QueryBuilders.matchQuery(User.LOGIN_ID, multiLoginId));
		orQueryBuilder.should(QueryBuilders.matchQuery(User.USER_MOBILE, multiLoginId));
		orQueryBuilder.should(QueryBuilders.matchQuery(User.USER_EMAIL, multiLoginId));
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(orQueryBuilder);
		boolQueryBuilder.must(QueryBuilders.matchQuery(User.USER_PSW, userPsw));
		
		Iterable<User> user = this.getRepository().search(boolQueryBuilder);
		if(user.iterator().hasNext()){
			return user.iterator().next();
		}else{
			return null;
		}
	}
}
