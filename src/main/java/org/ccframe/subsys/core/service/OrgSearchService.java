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
import org.ccframe.subsys.bike.service.CyclingOrderService;
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
			//this.SumQuery(queryBuilder, fieldNames)
			
			OrgRowDto orgRowDto = new OrgRowDto();
			BeanUtils.copyProperties(org, orgRowDto);

			// 查询出充值总金额
			boolQueryBuilder = QueryBuilders.boolQuery();
			boolQueryBuilder.must(QueryBuilders.termQuery(ChargeOrder.CHARGE_ORDER_STAT_CODE, ChargeOrderStatCodeEnum.CHARGE_SUCCESS.toCode()));
			boolQueryBuilder.must(QueryBuilders.termQuery(ChargeOrder.ORG_ID, org.getOrgId()));
			orgRowDto.setChargeTotalValue(SpringContextHelper.getBean(ChargeOrderSearchService.class).sumQuery(boolQueryBuilder, ChargeOrder.CHARGE_AMMOUNT));

			//TODO yjz完成其它查询
			
//			List<ChargeOrder> chargeOrders = SpringContextHelper.getBean(ChargeOrderService.class).findByKey(ChargeOrder.ORG_ID, org.getOrgId());
//			Double chargeTotalValue = 0.0;
//			for (ChargeOrder chargeOrder : chargeOrders) {
//				// 充值成功的状态
//				if (ChargeOrderStatCodeEnum.CHARGE_SUCCESS.toCode().equals(chargeOrder.getChargeOrderStatCode())) {
//					chargeTotalValue += chargeOrder.getChargeAmmount();
//				}
//			}
//			orgRowDto.setChargeTotalValue(chargeTotalValue);
			// 查询出骑行订单
			List<CyclingOrder> cyclingOrders = SpringContextHelper.getBean(CyclingOrderService.class).findByKey(CyclingOrder.ORG_ID, org.getOrgId());
			Double cyclingIncome = 0.0;
			Integer cyclingNum = 0;
			for (CyclingOrder cyclingOrder : cyclingOrders) {
				// 骑行完成的状态
				if (CyclingOrderStatCodeEnum.CYCLING_FINISH.toCode().equals(cyclingOrder.getCyclingOrderStatCode())) {
					cyclingIncome += cyclingOrder.getOrderAmmount();
					cyclingNum++;
				}
			}
			orgRowDto.setCyclingIncome(cyclingIncome);
			orgRowDto.setCyclingNum(cyclingNum);
			resultList.add(orgRowDto); 
		}
		return new ClientPage<OrgRowDto>((int)page.getTotalElements(), offset / limit, limit, resultList);
	}

	
}
