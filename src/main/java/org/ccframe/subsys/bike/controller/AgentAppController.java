
package org.ccframe.subsys.bike.controller;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.AgentApp;
import org.ccframe.subsys.bike.dto.AgentAppListReq;
import org.ccframe.subsys.bike.dto.AgentAppRowDto;
import org.ccframe.subsys.bike.service.AgentAppSearchService;
import org.ccframe.subsys.bike.service.AgentAppService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.AGENT_APP_BASE)
public class AgentAppController{
	@RequestMapping(value = Global.ID_BINDER_PATH)
	public AgentApp getAgentApp(@PathVariable(Global.ID_BINDER_ID) Integer agentAppId) {
		return SpringContextHelper.getBean(AgentAppService.class).getById(agentAppId);
	}

	@RequestMapping(value = Global.ID_BINDER_PATH, method=RequestMethod.DELETE)
	public void delete(@PathVariable(Global.ID_BINDER_ID) java.lang.Integer agentAppId){
		SpringContextHelper.getBean(AgentAppService.class).deleteById(agentAppId);
	}

	@RequestMapping(value = ControllerMapping.AGENT_APP_LIST, method = RequestMethod.POST)
	public ClientPage<AgentAppRowDto> findAgentAppList(@RequestBody AgentAppListReq agentAppListReq, int offset, int limit) {
		return SpringContextHelper.getBean(AgentAppSearchService.class).findAgentAppList(agentAppListReq, offset, limit);
	}

	@RequestMapping(method=RequestMethod.POST)
	public void saveOrUpdate(@RequestBody AgentApp agentApp){
		SpringContextHelper.getBean(AgentAppService.class).saveOrUpdateAgentApp(agentApp);
	}
}

