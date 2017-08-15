package org.ccframe.subsys.core.service;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.commons.base.OffsetBasedPageRequest;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.domain.entity.MemberAccount;
import org.ccframe.subsys.core.domain.entity.MemberAccountLog;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.dto.MemberAccountLogListReq;
import org.ccframe.subsys.core.dto.MemberAccountLogRowDto;
import org.ccframe.subsys.core.search.MemberAccountLogSearchRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class MemberAccountLogSearchService extends BaseSearchService<MemberAccountLog, Integer, MemberAccountLogSearchRepository> {

	public ClientPage<MemberAccountLogRowDto> findMemberAccountLogList(MemberAccountLogListReq memberAccountLogListReq, int offset,	int limit) {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		// 过滤账户类型
		if (memberAccountLogListReq.getMemberAccountId() != null) {
			MemberAccount memberAccount = SpringContextHelper.getBean(MemberAccountSearchService.class).getById(memberAccountLogListReq.getMemberAccountId());
			boolQueryBuilder.must(QueryBuilders.termQuery(MemberAccountLog.MEMBER_ACCOUNT_ID, memberAccount.getMemberAccountId()));
		}
		
		// 查询
		Page<MemberAccountLog> cPage = this.getRepository().search(
			boolQueryBuilder,
			new OffsetBasedPageRequest(offset, limit, new Order(Direction.DESC, MemberAccountLog.SYS_TIME))
		);
		
		List<MemberAccountLogRowDto> resultList = new ArrayList<MemberAccountLogRowDto>();
		for(MemberAccountLog memberAccountLog : cPage.getContent()){
			
			MemberAccountLogRowDto memberAccountLogRowDto = new MemberAccountLogRowDto();
			BeanUtils.copyProperties(memberAccountLog, memberAccountLogRowDto);
			// 查询操作人员
			User user = SpringContextHelper.getBean(UserSearchService.class).getById(memberAccountLog.getOperationManId());
			if (user!=null) {
				memberAccountLogRowDto.setOperationMan(user.getUserNm());
			}
			resultList.add(memberAccountLogRowDto);
		}
		return new ClientPage<MemberAccountLogRowDto>((int)cPage.getTotalElements(), offset / limit, limit, resultList);
	}

}
