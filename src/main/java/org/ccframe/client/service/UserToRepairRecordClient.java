package org.ccframe.client.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.ClientPage;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.subsys.bike.dto.UserToRepairRecordListReq;
import org.ccframe.subsys.bike.dto.UserToRepairRecordRowDto;
import org.fusesource.restygwt.client.RestService;

public interface UserToRepairRecordClient extends RestService{

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.USER_TO_REPAIR_RECORD_BASE + "/"+ ControllerMapping.USER_TO_REPAIR_RECORD_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findList(UserToRepairRecordListReq userToRepairRecordListReq, @QueryParam("offset") int offset, @QueryParam("limit") int limit, RestCallback<ClientPage<UserToRepairRecordRowDto>> callback);

}
