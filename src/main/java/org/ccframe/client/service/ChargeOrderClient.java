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
import org.ccframe.subsys.bike.domain.entity.ChargeOrder;
import org.ccframe.subsys.bike.dto.ChargeOrderListReq;
import org.ccframe.subsys.bike.dto.ChargeOrderRowDto;
import org.fusesource.restygwt.client.RestService;

public interface ChargeOrderClient extends RestService{
	
	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.CHARGE_ORDER_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void getById(@PathParam(Global.ID_BINDER_ID) Integer chargeOrderId, RestCallback<ChargeOrder> callback);
	
	@DELETE @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.CHARGE_ORDER_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void delete(@PathParam(Global.ID_BINDER_ID) Integer chargeOrderId, RestCallback<Void> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.CHARGE_ORDER_BASE + "/"+ ControllerMapping.CHARGE_ORDER_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findList(ChargeOrderListReq chargeOrderListReq, @QueryParam("offset") int offset, @QueryParam("limit") int limit, RestCallback<ClientPage<ChargeOrderRowDto>> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.CHARGE_ORDER_BASE + Global.REST_REQUEST_URL_SUFFIX)
	void saveOrUpdate(ChargeOrder chargeOrder, RestCallback<Void> restCallback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.CHARGE_ORDER_BASE + "/"+ ControllerMapping.CHARGE_ORDER_EXPORT + Global.REST_REQUEST_URL_SUFFIX)
	void exportUrl(String tempFilePath, RestCallback<String> restCallback);
}
