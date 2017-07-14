package org.ccframe.client.service;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.client.components.LabelValue;
import org.fusesource.restygwt.client.RestService;

public interface SimpleLabelValueClient extends RestService{
	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.SIMPLE_LABEL_VALUE_BASE + Global.REST_REQUEST_URL_SUFFIX)
	void getLabelValueList(@QueryParam("beanName") String beanName, @QueryParam("extraParam") String extraParam, RestCallback<List<LabelValue>> restCallback);

}
