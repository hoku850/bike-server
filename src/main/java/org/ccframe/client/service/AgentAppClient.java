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
import org.ccframe.subsys.bike.domain.entity.AgentApp;
import org.ccframe.subsys.bike.dto.AgentAppListReq;
import org.ccframe.subsys.bike.dto.AgentAppRowDto;
import org.fusesource.restygwt.client.RestService;

public interface AgentAppClient extends RestService{
	
	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.AGENT_APP_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void getById(@PathParam(Global.ID_BINDER_ID) Integer agentAppId, RestCallback<AgentApp> callback);
	
	@DELETE @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.AGENT_APP_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void delete(@PathParam(Global.ID_BINDER_ID) Integer agentAppId, RestCallback<Void> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.AGENT_APP_BASE + "/"+ ControllerMapping.AGENT_APP_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findAgentAppList(AgentAppListReq agentAppListReq, @QueryParam("offset") int offset, @QueryParam("limit") int limit, RestCallback<ClientPage<AgentAppRowDto>> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.AGENT_APP_BASE + Global.REST_REQUEST_URL_SUFFIX)
	void saveOrUpdate(AgentApp agentApp, RestCallback<Void> restCallback);
}
