package org.ccframe.client.service;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.RestCallback;
import org.fusesource.restygwt.client.RestService;

public interface FileInfClient extends RestService{

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_FILE_INF_BASE + "/"+ ControllerMapping.ADMIN_FILE_INF_START_IMPORT + Global.REST_REQUEST_URL_SUFFIX)
	void startImport(@QueryParam("filePath") String filePath, @QueryParam("beanName") String beanName, RestCallback<String> restCallback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_FILE_INF_BASE + "/"+ ControllerMapping.ADMIN_FILE_INF_QUERY_IMPORT + Global.REST_REQUEST_URL_SUFFIX)
	void queryImport(@QueryParam("filePath") String tempFilePath, @QueryParam("beanName") String beanName, RestCallback<Double> restCallback);

}
