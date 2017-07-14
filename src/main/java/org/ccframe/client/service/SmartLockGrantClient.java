package org.ccframe.client.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.subsys.bike.dto.SmartLockGrant;
import org.fusesource.restygwt.client.RestService;

public interface SmartLockGrantClient extends RestService{

//	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.SMART_LOCK_GRANT_BASE + Global.REST_REQUEST_URL_SUFFIX)
//	void grant(SmartLockGrant smartLockGrant, RestCallback<Void> restCallback);
	

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.SMART_LOCK_BASE + "/"+ ControllerMapping.SMART_LOCK_SEARCH_LIST + Global.REST_REQUEST_URL_SUFFIX)
	long grantSearch(SmartLockGrant smartLockGrant);
	
	
	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.SMART_LOCK_BASE + "/"+ ControllerMapping.SMART_LOCK_GRANT_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void grant(SmartLockGrant smartLockGrant, RestCallback<Void> restCallback);
}
