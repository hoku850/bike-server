package org.ccframe.sdk.bike.websocket;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {  
    
    private Logger logger = Logger.getLogger(getClass());  
      
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,  
            ServerHttpResponse response, WebSocketHandler wsHandler,  
            Map<String, Object> attributes) throws Exception {  
        //logger.debug("GOMA ===> Before Handshake");  
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