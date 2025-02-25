package recipe.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Component
public class WebSocketEventListener {
	@EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("새로운 웹소켓 연결 감지");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        log.info("웹소켓 연결 종료됨");
    }

}
