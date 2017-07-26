
package org.ccframe.subsys.core.controller;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.domain.entity.Org;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.dto.OrgListReq;
import org.ccframe.subsys.core.dto.OrgRowDto;
import org.ccframe.subsys.core.service.OrgSearchService;
import org.ccframe.subsys.core.service.OrgService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.ORG_BASE)
public class OrgController{
	
	@RequestMapping(value = Global.ID_BINDER_PATH)
	public Org getOrg(@PathVariable(Global.ID_BINDER_ID) Integer agentId) {
		return SpringContextHelper.getBean(OrgService.class).getById(agentId);
	}

	@RequestMapping(value = Global.ID_BINDER_PATH, method=RequestMethod.DELETE)
	public void delete(@PathVariable(Global.ID_BINDER_ID) java.lang.Integer agentId){
		SpringContextHelper.getBean(OrgService.class).deleteById(agentId);
	}

	@RequestMapping(value = ControllerMapping.ORG_LIST, method = RequestMethod.POST)
	public ClientPage<OrgRowDto> findOrgList(@RequestBody OrgListReq agentListReq, int offset, int limit) {
		return SpringContextHelper.getBean(OrgSearchService.class).findList(agentListReq, offset, limit);
	}

	@RequestMapping(method=RequestMethod.POST)
	public User saveOrUpdate(@RequestBody Org org){
		return SpringContextHelper.getBean(OrgService.class).saveOrUpdateOrg(org);
	}
}

