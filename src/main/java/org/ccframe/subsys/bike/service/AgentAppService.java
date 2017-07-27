package org.ccframe.subsys.bike.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ccframe.client.Global;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.sdk.bike.utils.AppConstant;
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
	public Map<String, Object> getDeposit() {
	
		MemberUser user = (MemberUser) WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
		List<AgentApp> list = SpringContextHelper.getBean(AgentAppService.class).findByKey(AgentApp.ORG_ID, user.getOrgId());
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(list!=null && list.size()>0) {	

			map.put(AppConstant.DEPOSIT, list.get(0).getChargeDeposit());

		}

		return map;
	}
}
