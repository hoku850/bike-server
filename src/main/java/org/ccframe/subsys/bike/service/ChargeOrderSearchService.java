package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.commons.base.OffsetBasedPageRequest;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.ChargeOrder;
import org.ccframe.subsys.bike.dto.ChargeOrderListReq;
import org.ccframe.subsys.bike.dto.ChargeOrderRowDto;
import org.ccframe.subsys.bike.search.ChargeOrderSearchRepository;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.service.OrgService;
import org.ccframe.subsys.core.service.UserService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class ChargeOrderSearchService extends BaseSearchService<ChargeOrder, Integer, ChargeOrderSearchRepository>{

	public ClientPage<ChargeOrderRowDto> findChargeOrderList(ChargeOrderListReq chargeOrderListReq, int offset, int limit) {
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		// 过滤运营商
		Integer orgId = chargeOrderListReq.getOrgId();
		if(orgId != null && orgId != 0){
			boolQueryBuilder.must(QueryBuilders.termQuery(ChargeOrder.ORG_ID, orgId));
		}
		// 过滤充值订单状态
		if(StringUtils.isNotBlank(chargeOrderListReq.getChargeOrderStatCode())){
			boolQueryBuilder.must(QueryBuilders.termQuery(ChargeOrder.CHARGE_ORDER_STAT_CODE, chargeOrderListReq.getChargeOrderStatCode()));
		}
		// 过滤支付类型
		if(StringUtils.isNotBlank(chargeOrderListReq.getPaymentTypeCode())){
			boolQueryBuilder.must(QueryBuilders.termQuery(ChargeOrder.PAYMENT_TYPE_CODE, chargeOrderListReq.getPaymentTypeCode()));
		}
		// 过滤[输入登陆ID/订单号码]
		if (StringUtils.isNotBlank(chargeOrderListReq.getSearchText())) {
			BoolQueryBuilder shouldQueryBuilder = QueryBuilders.boolQuery();
			
			User user = SpringContextHelper.getBean(UserService.class).getByKey(User.LOGIN_ID, chargeOrderListReq.getSearchText());
			if(user != null){
				shouldQueryBuilder.should(QueryBuilders.termQuery(ChargeOrder.USER_ID, user.getUserId()));
			}
			shouldQueryBuilder.should(QueryBuilders.termQuery(ChargeOrder.CHARGE_ORDER_NUM, chargeOrderListReq.getSearchText()));
			boolQueryBuilder.must(shouldQueryBuilder);
		}
				
		// 查询
		Page<ChargeOrder> cPage = this.getRepository().search(
			boolQueryBuilder,
			new OffsetBasedPageRequest(offset, limit, new Order(Direction.DESC, ChargeOrder.CHARGE_FINISH_TIME))
		);
		
		List<ChargeOrderRowDto> resultList = new ArrayList<ChargeOrderRowDto>();
		for(ChargeOrder chargeOrder : cPage.getContent()){
			
			ChargeOrderRowDto chargeOrderRowDto = new ChargeOrderRowDto();
			BeanUtils.copyProperties(chargeOrder, chargeOrderRowDto);
			
			User user = SpringContextHelper.getBean(UserService.class).getById(chargeOrder.getUserId());
			if (user != null) {
				chargeOrderRowDto.setLoginId(user.getLoginId());
			}
			// 查询出运营商的信息
			Org org = SpringContextHelper.getBean(OrgService.class).getById(chargeOrder.getOrgId());
			if (org != null) {
				chargeOrderRowDto.setOrgNm(org.getOrgNm());
			}
			resultList.add(chargeOrderRowDto);
		}
		return new ClientPage<ChargeOrderRowDto>((int)cPage.getTotalElements(), offset / limit, limit, resultList);
	}

	public List<ChargeOrder> findByUserIdAndOrgIdOrderByChargeFinishTimeDesc(Integer userId, Integer orgId) {
		return this.getRepository().findByUserIdAndOrgIdOrderByChargeFinishTimeDesc(userId, orgId);
	}
}
