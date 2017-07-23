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
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.dto.OrgDto;
import org.ccframe.subsys.core.dto.OrgListReq;
import org.ccframe.subsys.core.dto.OrgRowDto;
import org.fusesource.restygwt.client.RestService;

/**
 * 由于直接使用Org类过不了restyGWT，因此增加个OrgStub子类来替代
 * @author JIM
 *
 */
public interface OrgClient extends RestService{
	
	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ORG_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void getById(@PathParam(Global.ID_BINDER_ID) Integer agentId, RestCallback<OrgDto> callback);
	
	@DELETE @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ORG_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void delete(@PathParam(Global.ID_BINDER_ID) Integer agentId, RestCallback<Void> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ORG_BASE + "/"+ ControllerMapping.ORG_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findAgentList(OrgListReq agentListReq, @QueryParam("offset") int offset, @QueryParam("limit") int limit, RestCallback<ClientPage<OrgRowDto>> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ORG_BASE + Global.REST_REQUEST_URL_SUFFIX)
	void saveOrUpdate(OrgDto orgDto, RestCallback<User> restCallback);
}
