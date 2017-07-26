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
import org.ccframe.subsys.bike.domain.entity.CyclingOrder;
import org.ccframe.subsys.bike.dto.CyclingOrderListReq;
import org.ccframe.subsys.bike.dto.CyclingOrderRowDto;
import org.fusesource.restygwt.client.RestService;

public interface CyclingOrderClient extends RestService{
	
	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.CYCLING_ORDER_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void getDtoById(@PathParam(Global.ID_BINDER_ID) Integer cyclingOrderId, RestCallback<CyclingOrderRowDto> callback);
	
	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.CYCLING_ORDER_BASE + "/"+ ControllerMapping.CYCLING_ORDER_FINISH + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void finishGetById(@PathParam(Global.ID_BINDER_ID) Integer cyclingOrderId, RestCallback<CyclingOrderRowDto> callback);
	
	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.CYCLING_ORDER_BASE + "/"+ ControllerMapping.CYCLING_ORDER_FINISH + Global.REST_REQUEST_URL_SUFFIX)
	void finish(CyclingOrderRowDto cyclingOrderRowDto, RestCallback<Integer> restCallback);
	
	@DELETE @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.CYCLING_ORDER_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void delete(@PathParam(Global.ID_BINDER_ID) Integer cyclingOrderId, RestCallback<Void> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.CYCLING_ORDER_BASE + "/"+ ControllerMapping.CYCLING_ORDER_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findList(CyclingOrderListReq cyclingOrderListReq, @QueryParam("offset") int offset, @QueryParam("limit") int limit, RestCallback<ClientPage<CyclingOrderRowDto>> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.CYCLING_ORDER_BASE + Global.REST_REQUEST_URL_SUFFIX)
	void saveOrUpdate(CyclingOrder cyclingOrder, RestCallback<Void> restCallback);
	
	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.CYCLING_ORDER_BASE + "/"+ ControllerMapping.CYCLING_ORDER_EXPORT + Global.REST_REQUEST_URL_SUFFIX)
	void exportUrl(Integer orgId, RestCallback<String> restCallback);

}
