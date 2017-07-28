package org.ccframe.client.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.subsys.core.domain.entity.MenuRes;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.dto.MainFrameResp;
import org.fusesource.restygwt.client.RestService;

/**
 * URL请求资源定义
 * 
 * @author JIM
 * 
 * 备注：@FormParam是POST绑定 @QueryParam是GET绑定
 *
 */
public interface MainFrameClient extends RestService{

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.MAIN_FRAME_BASE + "/"+ ControllerMapping.MAIN_FRAME_ADMIN_MENU + Global.REST_REQUEST_URL_SUFFIX)
    void getMainFrameDto(RestCallback<MainFrameResp> callback);

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.MAIN_FRAME_BASE + "/"+ ControllerMapping.MAIN_FRAME_DO_LOGOUT + Global.REST_REQUEST_URL_SUFFIX)
    void doLogout(RestCallback<String> callback);

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.MAIN_FRAME_BASE + "/"+ ControllerMapping.MAIN_FRAME_GET_MENU_RES + Global.REST_REQUEST_URL_SUFFIX)
	void getMenuRes(@QueryParam(MenuRes.MENU_RES_ID) Integer menuResId, RestCallback<MenuRes> callback);

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.MAIN_FRAME_BASE + "/"+ ControllerMapping.MAIN_FRAME_UPDATE_PASSWORD + Global.REST_REQUEST_URL_SUFFIX)
	void updatePassword(@QueryParam(User.USER_ID) Integer userId,@QueryParam(User.USER_PSW) String password, RestCallback<Void> restCallback);

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.MAIN_FRAME_BASE + "/"+ ControllerMapping.MAIN_FRAME_MENU_HIT + Global.REST_REQUEST_URL_SUFFIX)
	void menuHit(@QueryParam("viewResValue") String viewResValue,  RestCallback<Void> restCallbackt) ;
}
