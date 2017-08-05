package org.ccframe.client.service;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.subsys.bike.domain.entity.SmartLock;
import org.ccframe.subsys.bike.dto.SmartLockListReq;
import org.ccframe.subsys.bike.dto.SmartLockRowDto;
import org.fusesource.restygwt.client.RestService;

public interface SmartLockClient extends RestService{
	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.SMART_LOCK_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void getById(@PathParam(Global.ID_BINDER_ID) Integer smartLockId, RestCallback<SmartLock> callback);

	@DELETE @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.SMART_LOCK_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void decideDeleteById(@PathParam(Global.ID_BINDER_ID) Integer smartLockId, RestCallback<Void> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.SMART_LOCK_BASE + "/"+ ControllerMapping.SMART_LOCK_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findSmartLockList(SmartLockListReq smartLockListReq, @QueryParam("offset") int offset, @QueryParam("limit") int limit, RestCallback<ClientPage<SmartLockRowDto>> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.SMART_LOCK_BASE + Global.REST_REQUEST_URL_SUFFIX)
	void saveOrUpdate(SmartLock smartLock, RestCallback<Void> restCallback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.SMART_LOCK_BASE + "/"+ ControllerMapping.SMART_LOCK_EXPORT + Global.REST_REQUEST_URL_SUFFIX)
	void exportUrl(Integer orgId, RestCallback<String> restCallback);
	
	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.SMART_LOCK_BASE + "/"+ ControllerMapping.SMART_LOCK_DESERT + Global.REST_REQUEST_URL_SUFFIX)
	void doDesert(SmartLockRowDto selectedRow, RestCallback<Void> restCallback);
}
