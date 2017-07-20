package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.commons.base.OffsetBasedPageRequest;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.code.ChargeOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.code.CyclingOrderStatCodeEnum;
import org.ccframe.subsys.bike.domain.entity.Agent;
import org.ccframe.subsys.bike.domain.entity.ChargeOrder;
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.dto.AgentListReq;
import org.ccframe.subsys.bike.dto.AgentRowDto;
import org.ccframe.subsys.bike.search.AgentSearchRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;


@Service
public class AgentSearchService extends BaseSearchService<Agent, Integer, AgentSearchRepository>{

	public ClientPage<AgentRowDto> findAgentList(AgentListReq agentListReq,	int offset, int limit) {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		Page<Agent> page = this.getRepository().search(
			boolQueryBuilder,
			new OffsetBasedPageRequest(offset, limit, new Order(Direction.DESC, Agent.AGENT_ID))
		);

		List<AgentRowDto> resultList = new ArrayList<AgentRowDto>();
		for(Agent agent : page.getContent()){
			AgentRowDto agentRowDto = new AgentRowDto();
			BeanUtils.copyProperties(agent, agentRowDto);
			// 查询出充值总金额
			List<ChargeOrder> chargeOrders = SpringContextHelper.getBean(ChargeOrderService.class).findByKey(ChargeOrder.ORG_ID, agent.getAgentId());
			Double chargeTotalValue = 0.0;
			for (ChargeOrder chargeOrder : chargeOrders) {
				// 充值成功的状态
				if (ChargeOrderStatCodeEnum.CHARGE_SUCCESS.toCode().equals(chargeOrder.getChargeOrderStatCode())) {
					chargeTotalValue += chargeOrder.getChargeAmmount();
				}
			}
			agentRowDto.setChargeTotalValue(chargeTotalValue);
			// 查询出骑行订单
			List<CyclingOrder> cyclingOrders = SpringContextHelper.getBean(CyclingOrderService.class).findByKey(CyclingOrder.ORG_ID, agent.getAgentId());
			Double cyclingIncome = 0.0;
			Integer cyclingNum = 0;
			for (CyclingOrder cyclingOrder : cyclingOrders) {
				// 骑行完成的状态
				if (CyclingOrderStatCodeEnum.CYCLING_FINISH.toCode().equals(cyclingOrder.getCyclingOrderStatCode())) {
					cyclingIncome += cyclingOrder.getOrderAmmount();
					cyclingNum++;
				}
			}
			agentRowDto.setCyclingIncome(cyclingIncome);
			agentRowDto.setCyclingNum(cyclingNum);
			resultList.add(agentRowDto); 
		}
		return new ClientPage<AgentRowDto>((int)page.getTotalElements(), offset / limit, limit, resultList);
	}

	
}
