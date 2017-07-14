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
import org.ccframe.subsys.core.dto.UserListReq;
import org.ccframe.subsys.core.dto.UserRowDto;
import org.fusesource.restygwt.client.RestService;

public interface AdminUserClient extends RestService{

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_USER_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void getById(@PathParam(Global.ID_BINDER_ID) Integer userId, RestCallback<User> callback);

	@DELETE @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_USER_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void delete(@PathParam(Global.ID_BINDER_ID) Integer userId, RestCallback<Void> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_USER_BASE + "/"+ ControllerMapping.ADMIN_USER_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findUserList(UserListReq userListReq, @QueryParam("offset") int offset, @QueryParam("limit") int limit, RestCallback<ClientPage<UserRowDto>> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_USER_BASE + Global.REST_REQUEST_URL_SUFFIX)
	void saveOrUpdate(User adminUser, RestCallback<Void> restCallback);
}
