package org.ccframe.client.service;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.subsys.core.domain.entity.Role;
import org.ccframe.subsys.core.domain.entity.User;
import org.fusesource.restygwt.client.RestService;

public interface AdminRoleClient extends RestService{

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_ROLE_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void getById(@PathParam(Global.ID_BINDER_ID) Integer roleId, RestCallback<Role> callback);

	@DELETE @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_ROLE_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void delete(@PathParam(Global.ID_BINDER_ID) Integer roleId, RestCallback<Void> callback);

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_ROLE_BASE + "/"+ ControllerMapping.ADMIN_ROLE_REF_USER_COUNT + Global.REST_REQUEST_URL_SUFFIX)
	void getRefUserCount(@QueryParam("roleId") Integer roleId, RestCallback<Integer> callback);

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_ROLE_BASE + "/"+ ControllerMapping.ADMIN_ROLE_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findRoleList(RestCallback<List<Role>> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_ROLE_BASE + Global.REST_REQUEST_URL_SUFFIX)
	void saveOrUpdate(Role role, RestCallback<Void> restCallback);

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_ROLE_BASE + "/"+ ControllerMapping.ADMIN_ROLE_SYS_TREE_NODE_ID_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findRoleSysTreeNodeIdList(@QueryParam("roleId") Integer roleId, RestCallback<List<Integer>> sysTreeNodeIdList);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_ROLE_BASE + "/"+ ControllerMapping.ADMIN_ROLE_SAVE_ROLE_MENU_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void saveRoleMenuList(List<Integer> sysMenuResIdList, @QueryParam("roleId") Integer roleId, RestCallback<Void> restCallback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_ROLE_BASE + "/"+ ControllerMapping.ADMIN_ROLE_USER_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findRoleUserList(@QueryParam("roleId") Integer roleId, RestCallback<List<User>> restCallback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_ROLE_BASE + "/"+ ControllerMapping.ADMIN_ROLE_BATCH_DELETE_USER_ROLE_REL + Global.REST_REQUEST_URL_SUFFIX)
	void batchDeleteRoleUserRel(@QueryParam("roleId") Integer roleId, List<Integer> userIdList, RestCallback<Void> restCallback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_ROLE_BASE + "/"+ ControllerMapping.ADMIN_ROLE_BATCH_ADD_USER_ROLE_REL + Global.REST_REQUEST_URL_SUFFIX)
	void batchAddRoleUserRel(@QueryParam("roleId") Integer roleId, List<String> loginIdList, RestCallback<List<String>> restCallback);
}
