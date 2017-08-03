
package org.ccframe.subsys.bike.controller;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.commons.util.JsonBinder;
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
	public String grant(@RequestBody SmartLockGrant smartLockGrant){
		SpringContextHelper.getBean(SmartLockSearchService.class).grant(smartLockGrant);
		return JsonBinder.buildNormalBinder().toJson(Global.SPRING_MVC_JSON_SUCCESS);
	}
	
	@RequestMapping(value = ControllerMapping.SMART_LOCK_SEARCH_LIST, method = RequestMethod.POST)
	public long grantSearch(@RequestBody SmartLockGrant smartLockGrant) {
		return SpringContextHelper.getBean(SmartLockSearchService.class).grantSearch(smartLockGrant);
	}
	
	@RequestMapping(value = ControllerMapping.SMART_LOCK_QUERY_GRANT, method=RequestMethod.POST)
	public double queryGrant(){
		Double result = SpringContextHelper.getBean(SmartLockSearchService.class).getGrantStatusMap().get("grantPercent");
		System.out.println(result);
		return result == null ? Global.IMPORT_INIT : result;
	}
}

