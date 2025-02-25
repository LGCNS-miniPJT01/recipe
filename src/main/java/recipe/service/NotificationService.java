package recipe.service;

import java.util.List;

import recipe.entity.Notification;
import recipe.entity.Recipe;
import recipe.entity.User;

// 알림 기능 관련 서비스
public interface NotificationService {
	// 알림 생성 기능
	void sendNotification(User sender, User receiver, Recipe recipe, String message);
	// 알림 보내기 기능
	List<Notification> getUnreadNotifications(User receiver);
}
