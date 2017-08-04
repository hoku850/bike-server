package org.ccframe.client.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.subsys.bike.dto.SmartLockGrant;
import org.fusesource.restygwt.client.RestService;

public interface SmartLockGrantClient extends RestService{
	//进度条
	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.SMART_LOCK_BASE + "/"+ ControllerMapping.SMART_LOCK_QUERY_GRANT + Global.REST_REQUEST_URL_SUFFIX)
	void queryGrant(RestCallback<Double> restCallback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.SMART_LOCK_BASE + "/"+ ControllerMapping.SMART_LOCK_SEARCH_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void grantSearch(SmartLockGrant smartLockGrant, RestCallback<Long> restCallback);
	
	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.SMART_LOCK_BASE + "/"+ ControllerMapping.SMART_LOCK_GRANT_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void grant(SmartLockGrant smartLockGrant, RestCallback<String> restCallback);
}
