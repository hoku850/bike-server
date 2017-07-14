package org.ccframe.client.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.ccframe.client.ControllerMapping;
import org.ccframe.client.Global;
import org.ccframe.client.commons.RestCallback;
import org.ccframe.subsys.core.dto.CacheInf;
import org.fusesource.restygwt.client.RestService;

public interface CacheInfClient extends RestService{

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_CACHE_BASE + "/"+ ControllerMapping.ADMIN_CACHE_INF_LIST + Global.REST_REQUEST_URL_SUFFIX)
	void findCacheList(RestCallback<List<CacheInf>> restCallback);

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ADMIN_CACHE_BASE + "/"+ ControllerMapping.ADMIN_CACHE_CLEAR_CACHES + Global.REST_REQUEST_URL_SUFFIX)
	void clearCaches(@QueryParam("cacheNameStr") String cacheNameStr, RestCallback<Void> restCallback);

}
