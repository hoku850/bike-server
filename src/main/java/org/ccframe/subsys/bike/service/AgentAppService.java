package org.ccframe.subsys.bike.service;

import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.AgentApp;
import org.ccframe.subsys.bike.repository.AgentAppRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentAppService extends BaseService<AgentApp,java.lang.Integer, AgentAppRepository> {
	
	@Transactional
	public void saveOrUpdateAgentApp(AgentApp agentApp) {
		SpringContextHelper.getBean(AgentAppService.class).save(agentApp);
	}
}
