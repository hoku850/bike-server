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
import org.ccframe.subsys.core.dto.MemberAccountListReq;
import org.ccframe.subsys.core.dto.MemberAccountRowDto;
import org.fusesource.restygwt.client.RestService;

public interface MemberAccountClient extends RestService{
	
	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.MEMBER_ACCOUNT_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void getById(@PathParam(Global.ID_BINDER_ID) Integer memberAccountId, RestCallback<MemberAccountRowDto> callback);
	
	@DELETE @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.MEMBER_ACCOUNT_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void delete(@PathParam(Global.ID_BINDER_ID) Integer memberAccountId, RestCallback<Void> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.MEMBER_ACCOUNT_BASE + "/"+ ControllerMapping.MEMBER_ACCOUNT_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findList(MemberAccountListReq memberAccountListReq, @QueryParam("offset") int offset, @QueryParam("limit") int limit, RestCallback<ClientPage<MemberAccountRowDto>> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.MEMBER_ACCOUNT_BASE + Global.REST_REQUEST_URL_SUFFIX)
	void saveOrUpdate(MemberAccountRowDto memberAccountRowDto, RestCallback<Void> restCallback);
}
