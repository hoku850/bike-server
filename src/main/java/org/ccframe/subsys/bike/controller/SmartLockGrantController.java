
package org.ccframe.subsys.bike.controller;

import org.ccframe.client.ControllerMapping;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.dto.SmartLockGrant;
import org.ccframe.subsys.bike.service.SmartLockSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ControllerMapping.SMART_LOCK_BASE)
public class SmartLockGrantController{

	@RequestMapping(value = ControllerMapping.SMART_LOCK_GRANT_LIST, method = RequestMethod.POST)
	public void grant(@RequestBody SmartLockGrant smartLockGrant){
		SpringContextHelper.getBean(SmartLockSearchService.class).grant(smartLockGrant);
	}
	
	@RequestMapping(value = ControllerMapping.SMART_LOCK_SEARCH_LIST, method = RequestMethod.POST)
	public long grantSearch(@RequestBody SmartLockGrant smartLockGrant) {
		return SpringContextHelper.getBean(SmartLockSearchService.class).grantSearch(smartLockGrant);
	}
	
}

