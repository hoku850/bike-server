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
import org.ccframe.client.commons.TreeNodeTree;
import org.fusesource.restygwt.client.RestService;

public interface TreeNodeClient extends RestService{

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.TREE_NODE_BASE + "/"+ ControllerMapping.TREE_NODE_TREE + Global.REST_REQUEST_URL_SUFFIX)
	void getTree(@QueryParam("treeRoot") Integer treeRoot, RestCallback<TreeNodeTree> callback);

	/**
	 * 获得Role关联的菜单树.
	 * 如果Role为机构id，则获取机构菜单模板。为空为获取当前机构下的菜单树
	 * @param treeRoot
	 * @param callback
	 */
	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.TREE_NODE_BASE + "/"+ ControllerMapping.TREE_NODE_ORG_MENU_TREE + Global.REST_REQUEST_URL_SUFFIX)
	void getOrgMenuTree(@QueryParam("orgId") Integer orgId, RestCallback<TreeNodeTree> callback);

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.TREE_NODE_BASE + "/"+ ControllerMapping.TREE_NODE_SUB_TREE + Global.REST_REQUEST_URL_SUFFIX)
	void getSubTree(@QueryParam("treeRoot") Integer treeRoot, RestCallback<List<TreeNodeTree>> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.TREE_NODE_BASE + Global.REST_REQUEST_URL_SUFFIX)
	void addModify(TreeNodeTree treeNodeTree, RestCallback<Void> callback);
	
	@DELETE @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.TREE_NODE_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void delete(@PathParam(Global.ID_BINDER_ID) Integer treeNodeId, @QueryParam("deleteCheckBeans") String deleteCheckBeans, RestCallback<Void> callback);
	
	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.TREE_NODE_BASE + "/" +  ControllerMapping.GET_NAME_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void getNamePath(@QueryParam("treeNodeId") Integer treeNodeId, @QueryParam("rootId") Integer rootId, RestCallback<List<String>> callback);
}
