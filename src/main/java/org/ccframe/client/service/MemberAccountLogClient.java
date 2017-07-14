package org.ccframe.client.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.subsys.core.dto.MemberAccountLogListReq;
import org.ccframe.subsys.core.dto.MemberAccountLogRowDto;
import org.fusesource.restygwt.client.RestService;

public interface MemberAccountLogClient extends RestService{

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.MEMBER_ACCOUNT_LOG_BASE + "/"+ ControllerMapping.MEMBER_ACCOUNT_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findList(MemberAccountLogListReq memberAccountLogListReq, @QueryParam("offset") int offset, @QueryParam("limit") int limit, RestCallback<ClientPage<MemberAccountLogRowDto>> callback);
}
