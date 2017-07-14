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
import org.ccframe.subsys.article.domain.entity.ArticleInf;
import org.ccframe.subsys.article.dto.ArticleInfDto;
import org.ccframe.subsys.article.dto.ArticleInfListReq;
import org.ccframe.subsys.article.dto.ArticleInfRowDto;
import org.fusesource.restygwt.client.RestService;

public interface ArticleInfClient extends RestService{

	/**
	 * @param articleInfListReq 查询条件
	 * @param offset
	 * @param limit
	 * @param callback
	 */
	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ARTICLE_BASE + "/"+ ControllerMapping.ARTICLE_LIST + Global.REST_REQUEST_URL_SUFFIX)
    void findArticleList(ArticleInfListReq articleInfListReq, @QueryParam("page") int page, @QueryParam("size") int size, RestCallback<ClientPage<ArticleInfRowDto>> callback);

	@GET @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ARTICLE_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void getById(@PathParam(Global.ID_BINDER_ID) Integer articleInfId, RestCallback<ArticleInfDto> callback);

	@POST @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ARTICLE_BASE + Global.REST_REQUEST_URL_SUFFIX)
    void saveOrUpdateArticleInfDto(ArticleInfDto articleInf, RestCallback<Void> callback);

	@DELETE @Path(ControllerMapping.CLIENT_TO_BASE + ControllerMapping.ARTICLE_BASE + Global.ID_BINDER_PATH + Global.REST_REQUEST_URL_SUFFIX)
	void delete(@PathParam(Global.ID_BINDER_ID) Integer articleInfId, RestCallback<Void> callback);

//	String ARTICLE_INF_DEL = "delete";
//	@POST @Path(ARTICLE_INF_DEL + Global.REST_REQUEST_URL_SUFFIX)
//    void delete(Integer articleInfId, MethodCallback<Integer> callback);
}
