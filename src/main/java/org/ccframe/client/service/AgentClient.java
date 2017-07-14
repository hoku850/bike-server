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
import org.ccframe.subsys.bike.domain.entity.Agent;
import org.ccframe.subsys.bike.dto.AgentListReq;
import org.ccframe.subsys.bike.dto.AgentRowDto;
import org.fusesource.restygwt.client.RestService;

public interface AgentClient extends RestService{
	
	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.AGENT_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void getById(@PathParam(Global.ID_BINDER_ID) Integer agentId, RestCallback<Agent> callback);
	
	@DELETE @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.AGENT_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void delete(@PathParam(Global.ID_BINDER_ID) Integer agentId, RestCallback<Void> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.AGENT_BASE + "/"+ ControllerMapping.AGENT_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findAgentList(AgentListReq agentListReq, @QueryParam("offset") int offset, @QueryParam("limit") int limit, RestCallback<ClientPage<AgentRowDto>> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.AGENT_BASE + Global.REST_REQUEST_URL_SUFFIX)
	void saveOrUpdate(Agent agent, RestCallback<Void> restCallback);
}
