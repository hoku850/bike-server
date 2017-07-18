
package org.ccframe.subsys.bike.controller;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.Agent;
import org.ccframe.subsys.bike.dto.AgentListReq;
import org.ccframe.subsys.bike.dto.AgentRowDto;
import org.ccframe.subsys.bike.service.AgentSearchService;
import org.ccframe.subsys.bike.service.AgentService;
import org.ccframe.subsys.core.domain.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.AGENT_BASE)
public class AgentController{
	
	@RequestMapping(value = Global.ID_BINDER_PATH)
	public Agent getAgentApp(@PathVariable(Global.ID_BINDER_ID) Integer agentId) {
		return SpringContextHelper.getBean(AgentService.class).getById(agentId);
	}

	@RequestMapping(value = Global.ID_BINDER_PATH, method=RequestMethod.DELETE)
	public void delete(@PathVariable(Global.ID_BINDER_ID) java.lang.Integer agentId){
		SpringContextHelper.getBean(AgentService.class).deleteById(agentId);
	}

	@RequestMapping(value = ControllerMapping.AGENT_LIST, method = RequestMethod.POST)
	public ClientPage<AgentRowDto> findAgentList(@RequestBody AgentListReq agentListReq, int offset, int limit) {
		return SpringContextHelper.getBean(AgentSearchService.class).findAgentList(agentListReq, offset, limit);
	}

	@RequestMapping(method=RequestMethod.POST)
	public User saveOrUpdate(@RequestBody Agent agent){
		return SpringContextHelper.getBean(AgentService.class).saveOrUpdateAgent(agent);
	}
}

