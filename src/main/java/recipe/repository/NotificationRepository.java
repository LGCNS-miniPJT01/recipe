package recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import recipe.entity.Notification;
import recipe.entity.User;

// 알림 기능 관련 repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverAndReadYnFalse(User receiver); // 읽지 않은 알림 조회
}
