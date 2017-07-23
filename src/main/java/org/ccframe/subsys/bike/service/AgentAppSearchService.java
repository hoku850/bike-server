package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.List;

import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.base.BaseSearchService;
import org.ccframe.commons.base.OffsetBasedPageRequest;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.AgentApp;
import org.ccframe.subsys.bike.dto.AgentAppListReq;
import org.ccframe.subsys.bike.dto.AgentAppRowDto;
import org.ccframe.subsys.bike.search.AgentAppSearchRepository;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.service.OrgService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@Service
public class AgentAppSearchService extends BaseSearchService<AgentApp, Integer, AgentAppSearchRepository>{

	public ClientPage<AgentAppRowDto> findAgentAppList(AgentAppListReq agentAppListReq, int offset, int limit) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
		
		Page<AgentApp> agentAppPage = this.getRepository().search(
			boolQueryBuilder,
			new OffsetBasedPageRequest(offset, limit, new Order(Direction.ASC, AgentApp.AGENT_APP_ID))
		);
		List<AgentAppRowDto> resultList = new ArrayList<AgentAppRowDto>();
		for(AgentApp agentApp:agentAppPage.getContent()){
			AgentAppRowDto rowRecord = new AgentAppRowDto();
			BeanUtils.copyProperties(agentApp, rowRecord);
			Org org = SpringContextHelper.getBean(OrgService.class).getById(agentApp.getOrgId());
			rowRecord.setOrgNm(org.getOrgNm());
			resultList.add(rowRecord);
		}
		return new ClientPage<AgentAppRowDto>((int)agentAppPage.getTotalElements(), offset / limit, limit, resultList);
	}
}
