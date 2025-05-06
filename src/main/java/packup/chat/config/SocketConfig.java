package packup.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class SocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 구독 경로(해당 prefix 들어와야 읽을 수 있음)
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/pub");    // 발행 경로

        // /user/queue/ > 회원 1명에게 전송하는 메서드 > 내부적으로 queue/{param}으로 읽음
        config.setUserDestinationPrefix("/user");            // convertAndSendToUser 전용
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/chat")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new SocketInterceptor());
    }
}

