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
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.dto.BikeTypeListReq;
import org.ccframe.subsys.bike.dto.BikeTypeRowDto;
import org.fusesource.restygwt.client.RestService;

public interface BikeTypeClient extends RestService{
	
	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.BIKE_TYPE_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void getById(@PathParam(Global.ID_BINDER_ID) Integer bikeTypeId, RestCallback<BikeType> callback);
	
	@DELETE @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.BIKE_TYPE_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void delete(@PathParam(Global.ID_BINDER_ID) Integer bikeTypeId, RestCallback<Void> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.BIKE_TYPE_BASE + "/"+ ControllerMapping.BIKE_TYPE_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findBikeTypeList(BikeTypeListReq bikeTypeListReq, @QueryParam("offset") int offset, @QueryParam("limit") int limit, RestCallback<ClientPage<BikeTypeRowDto>> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.BIKE_TYPE_BASE + Global.REST_REQUEST_URL_SUFFIX)
	void saveOrUpdate(BikeType bikeType, RestCallback<Void> restCallback);
}
