package org.ccframe.sdk.bike.websocket;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
  
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {  
  
    //private static Logger logger = LoggerFactory.getLogger(HandshakeInterceptor.class);  
    @Override  
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object  
                > attributes) throws Exception {  
        if (request instanceof ServletServerHttpRequest) {  
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;  
            HttpSession session = servletRequest.getServletRequest().getSession(false);  
            if (session != null) {  
                //使用userName区分WebSocketHandler，以便定向发送消息  
                String userName = (String) session.getAttribute("user");  
                if(userName != null){  
                    attributes.put("user",userName);  
                }  
            }  
        }  
        System.out.println("-----------beforeHandshake");  
        return true;  
    }  
  
    @Override  
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {  
    	System.out.println("afterHandshake");  
    }  
}  