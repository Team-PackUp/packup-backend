//package packup.chat.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.WebSocketMessage;
//import org.springframework.web.socket.WebSocketSession;
//
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//@Component
//public class SocketHandler implements WebSocketHandler {
////    private final ChatController chatController;
//    private final Set<WebSocketSession> sessionSet = new HashSet<>();
//    private final Map<Integer,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();
//    private RedisPublisher redisPublisher;
//
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        System.out.println("소켓 연결 성공");
//        this.sessionSet.add(session);
//    }
//
//    @Override
//    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
//        String payload = message.getPayload().toString();
//        System.out.println("Received message: " + payload);
//
//        redisPublisher.publishMessage("chatRoom1", payload);
//    }
//
//
//    @Override
//    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
//        boolean result = this.sessionSet.remove(session);
//
//        System.out.println("소켓 해제");
//
//        if(!result) {
//            throw new Exception("err test");
//        }
//    }
//
//    @Override
//    public boolean supportsPartialMessages() {
//        return false;
//    }
//}
