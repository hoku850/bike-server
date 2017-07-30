package org.ccframe.sdk.bike.websocket;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.service.UserSearchService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {  
    
    private Logger logger = Logger.getLogger(getClass());  
      
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,  
            ServerHttpResponse response, WebSocketHandler wsHandler,  
            Map<String, Object> attributes) throws Exception {  
        //logger.debug("GOMA ===> Before Handshake"); 
    	ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
		HttpSession session = servletRequest.getServletRequest().getSession(false);
		HttpServletRequest request2 = servletRequest.getServletRequest();
		String IMEI = request2.getParameter("IMEI");
		String phoneNumber = request2.getParameter("phoneNumber");
		System.out.println("IMEI："+IMEI+"   phoneNumber:"+phoneNumber);
		List<User> users = SpringContextHelper.getBean(UserSearchService.class).findByLoginIdAndUserPsw(phoneNumber, IMEI);
		 if(users!=null && users.size()>0) {
			 User user = users.get(0);
			 attributes.put("user", user);
			 System.out.println("websocket的user："+user);
		 }
		//User user = (User) session.getAttribute(Global.SESSION_LOGIN_MEMBER_USER);
        System.out.println("握手前调用");
        return super.beforeHandshake(request, response, wsHandler, attributes);  
    }  
  
    @Override  
    public void afterHandshake(ServerHttpRequest request,  
            ServerHttpResponse response, WebSocketHandler wsHandler,  
            Exception ex) {  
        //logger.debug("GOMA ===> After Handshake");  
        System.out.println("握手后调用");
        super.afterHandshake(request, response, wsHandler, ex);  
    }  
}  