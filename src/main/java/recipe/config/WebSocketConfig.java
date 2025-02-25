package recipe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
            .addEndpoint("/ws")  // 클라이언트가 연결할 엔드포인트
            .setAllowedOriginPatterns("*") ;// CORS 허용
//            .withSockJS(); // SockJS 지원
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");// 메시지 브로커 (구독 경로)
        registry.setUserDestinationPrefix("/user"); // 개인 메시지 전송을 위한 설정
        registry.setApplicationDestinationPrefixes("/app"); // 클라이언트에서 서버로 보낼 메시지 경로
    }
}

