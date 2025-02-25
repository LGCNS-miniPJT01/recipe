package recipe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import recipe.entity.Notification;
import recipe.entity.Recipe;
import recipe.entity.User;
import recipe.repository.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService {
	
	@Autowired
	private NotificationRepository	notificationRepository;
	
	@Autowired
    private SimpMessagingTemplate messagingTemplate;  // 웹소켓 메시지 전송을 위한 객체

	// 알림 생성 기능
	@Transactional
	@Override
	public void sendNotification(User sender, User receiver, Recipe recipe, String message) {
        
        // 1. DB에 알림 저장
        Notification notification = Notification.builder()
            .sender(sender)
            .receiver(receiver)
            .recipe(recipe)
            .message(message)
            .build();

        notificationRepository.save(notification);

        // 2. 웹소켓을 통해 실시간 알림 전송
        sendWebSocketNotification(receiver.getUsername(), message);
	}
	
	/**
     * 웹소켓을 통해 실시간 알림 전송
     */
    public void sendWebSocketNotification(String username, String message) {

        System.out.println(">>>>> Sending notification to user: " + username + " - " + message);
    
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", message);
    }

	// 알림 보내기 기능
	@Override
	public List<Notification> getUnreadNotifications(User receiver) {
        return notificationRepository.findByReceiverAndReadYnFalse(receiver);
	}

}
