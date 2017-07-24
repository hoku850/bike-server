package org.ccframe.sdk.bike.websocket;

import org.apache.log4j.Logger;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebsocketEndPoint extends TextWebSocketHandler {  
    
    private Logger logger = Logger.getLogger(getClass());  
      
    @Override  
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {  
          
        super.handleTextMessage(session, message);  
        //logger.debug("GOMA === > WebSocketEndPoint.handlerTextMessage..."); 
        
        System.out.println("客户端传来的内容：" + message.getPayload());  
        
        TextMessage returnMessage = new TextMessage(message.getPayload()+" received at server");  
        session.sendMessage(returnMessage);  
          
    }  
}  
