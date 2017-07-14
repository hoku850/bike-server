package org.ccframe.client.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.subsys.core.domain.entity.Param;
import org.ccframe.subsys.core.dto.ParamRowDto;
import org.fusesource.restygwt.client.RestService;

public interface ParamClient extends RestService{

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_PARAM_BASE + "/"+ ControllerMapping.ADMIN_PARAM_PREFERENCE_TEXT + Global.REST_REQUEST_URL_SUFFIX)
	void getPreferenceText(@QueryParam(Param.PARAM_INNER_CODING) String paramInnerCoding, RestCallback<List<String>> restCallback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_PARAM_BASE + "/"+ ControllerMapping.ADMIN_PARAM_PREFERENCE_TEXT + Global.REST_REQUEST_URL_SUFFIX)
	void setPreferenceText(List<String> preferenceTextList, @QueryParam(Param.PARAM_INNER_CODING) String paramInnerCoding, RestCallback<Void> restCallback);

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_PARAM_BASE + "/"+ ControllerMapping.ADMIN_PARAM_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findParamList(RestCallback<List<ParamRowDto>> restCallback);

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_PARAM_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void getById(@PathParam(Global.ID_BINDER_ID) Integer paramId, RestCallback<Param> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_PARAM_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void setParamValue(@PathParam(Global.ID_BINDER_ID) Integer paramId,@QueryParam(Param.PARAM_VALUE) String paramValue, RestCallback<Void> callback);
}
