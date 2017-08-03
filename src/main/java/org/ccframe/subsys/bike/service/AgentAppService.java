package org.ccframe.subsys.bike.service;

import java.util.List;

import org.ccframe.client.Global;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.sdk.bike.dto.AppPageDto;
import org.ccframe.subsys.bike.domain.entity.AgentApp;
import org.ccframe.subsys.bike.domain.entity.MemberUser;
import org.ccframe.subsys.bike.repository.AgentAppRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentAppService extends BaseService<AgentApp,java.lang.Integer, AgentAppRepository> {
	
	@Transactional
	public void saveOrUpdateAgentApp(AgentApp agentApp) {
		SpringContextHelper.getBean(AgentAppService.class).save(agentApp);
	}

	/**
	 * @author zjm
	 */
	@Transactional(readOnly=true)
	public AppPageDto getDeposit() {
	
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		List<AgentApp> list = SpringContextHelper.getBean(AgentAppService.class).findByKey(AgentApp.ORG_ID, user.getOrgId());
		
		AppPageDto appPageDto = new AppPageDto();
		if(list!=null && list.size()>0) {	
			appPageDto.setDeposit(list.get(0).getChargeDeposit()+"");
		}

		return appPageDto;
	}
}
