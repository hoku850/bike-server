package org.ccframe.subsys.core.service;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.commons.base.OffsetBasedPageRequest;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.code.ChargeOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.ChargeOrder;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.service.ChargeOrderSearchService;
import org.ccframe.subsys.bike.service.CyclingOrderSearchService;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.dto.OrgListReq;
import org.ccframe.subsys.core.dto.OrgRowDto;
import org.ccframe.subsys.core.search.OrgSearchRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;


@Service
public class OrgSearchService extends BaseSearchService<Org, Integer, OrgSearchRepository>{

	public ClientPage<OrgRowDto> findList(OrgListReq agentListReq,	int offset, int limit) {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		Page<Org> page = this.getRepository().search(
			boolQueryBuilder,
			new OffsetBasedPageRequest(offset, limit, new Order(Direction.ASC, Org.ORG_ID))
		);

		List<OrgRowDto> resultList = new ArrayList<OrgRowDto>();
		for(Org org : page.getContent()){
			
			OrgRowDto orgRowDto = new OrgRowDto();
			BeanUtils.copyProperties(org, orgRowDto);

			// 查询出充值总金额
			boolQueryBuilder = QueryBuilders.boolQuery();
			boolQueryBuilder.must(QueryBuilders.termQuery(ChargeOrder.CHARGE_ORDER_STAT_CODE, ChargeOrderStatCodeEnum.CHARGE_SUCCESS.toCode()));
			boolQueryBuilder.must(QueryBuilders.termQuery(ChargeOrder.ORG_ID, org.getOrgId()));
			orgRowDto.setChargeTotalValue(SpringContextHelper.getBean(ChargeOrderSearchService.class).sumQuery(boolQueryBuilder, ChargeOrder.CHARGE_AMMOUNT));
			
			// 查询出骑行订单数、骑行总收入
			boolQueryBuilder = QueryBuilders.boolQuery();
			boolQueryBuilder.must(QueryBuilders.termQuery(CyclingOrder.CYCLING_ORDER_STAT_CODE, CyclingOrderStatCodeEnum.PAY_FINISH.toCode()));
			boolQueryBuilder.must(QueryBuilders.termQuery(CyclingOrder.ORG_ID, org.getOrgId()));
			orgRowDto.setCyclingNum(SpringContextHelper.getBean(CyclingOrderSearchService.class).countQuery(boolQueryBuilder, CyclingOrder.CYCLING_ORDER_ID));
			orgRowDto.setCyclingIncome(SpringContextHelper.getBean(CyclingOrderSearchService.class).sumQuery(boolQueryBuilder, CyclingOrder.ORDER_AMMOUNT));
			
			resultList.add(orgRowDto); 
		}
		return new ClientPage<OrgRowDto>((int)page.getTotalElements(), offset / limit, limit, resultList);
	}
}
