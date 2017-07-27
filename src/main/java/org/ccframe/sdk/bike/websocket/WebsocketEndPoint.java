package org.ccframe.sdk.bike.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.ccframe.client.Global;
import org.ccframe.commons.util.WebContextHolder;
import org.ccframe.subsys.core.domain.entity.User;
import org.elasticsearch.snapshots.RestoreService.UpdateIndexShardRestoreStatusRequest;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebsocketEndPoint extends TextWebSocketHandler {  
	private static final Map<Integer, WebSocketSession> users;//用Map来存储，key用userid
    private Logger logger = Logger.getLogger(getClass());  
    static {
    	users = new HashMap<Integer, WebSocketSession>();
    }
    
    public WebsocketEndPoint() {
        // TODO Auto-generated constructor stub
    }

    /**
     * 连接成功时候，会触发页面上onopen方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	//User user = (User)WebContextHolder.getSessionContextStore().getServerValue(Global.SESSION_LOGIN_MEMBER_USER);
        // TODO Auto-generated method stub
        User user = (User) session.getAttributes().get("user");
        users.put(user.getUserId(),session);
        System.out.println("用户"+user.getUserId()+"正在用车！");
        System.out.println("connect to the websocket success......当前数量:"+users.size());
        //这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
        //TextMessage returnMessage = new TextMessage("你将收到的离线");
        //session.sendMessage(returnMessage);
    }
    
    /**
     * 关闭连接时触发
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        //logger.debug("websocket connection closed......");
    	User user = (User) session.getAttributes().get("user");
        
        System.out.println("用户"+user.getUserId()+"骑行已结束！");
        users.remove(user.getUserId());
        System.out.println("剩余在线用户"+users.size());
    }

    /**
     * js调用websocket.send时候，会调用该方法
     */
    @Override    
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        System.out.println("客户端传来的内容：" + message.getPayload());  
      
        TextMessage returnMessage = new TextMessage(message.getPayload()+" received at server");  
        session.sendMessage(returnMessage); 
    }

    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){session.close();}
        //logger.debug("websocket connection closed......");
        User user = (User) session.getAttributes().get("user");
        users.remove(user.getUserId());
    }

    public boolean supportsPartialMessages() {
        return false;
    }
    
    
    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public static void sendMessageToUser(Integer userId, TextMessage message) {
    	for(Integer userId2 : users.keySet()) {
    		if(userId2.equals(userId)) {
    			try {
    				WebSocketSession webSocketSession = users.get(userId);
                    if (webSocketSession.isOpen()) {
                    	webSocketSession.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
    		}
    	}
        
    }
    
    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
//    public void sendMessageToUsers(TextMessage message) {
//        for (WebSocketSession user : users) {
//            try {
//                if (user.isOpen()) {
//                    user.sendMessage(message);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
      
//    @Override  
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {  
//          
//        super.handleTextMessage(session, message);  
//        //logger.debug("GOMA === > WebSocketEndPoint.handlerTextMessage..."); 
//        
//        System.out.println("客户端传来的内容：" + message.getPayload());  
//        
//        TextMessage returnMessage = new TextMessage(message.getPayload()+" received at server");  
//        session.sendMessage(returnMessage);  
//          
//    }  
}  
