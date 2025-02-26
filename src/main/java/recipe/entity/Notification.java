package recipe.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 알림 기능 테이블
@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false) // 알림 받는 사람 (레시피 작성자)
    @JsonIgnore  // ✅ 순환 참조 방지
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "sender_id") // 알림을 보낸 사람 (댓글 작성자)
    @JsonIgnore  // ✅ 순환 참조 방지
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false) // 어떤 레시피에 대한 알림인지
    @JsonIgnore  // ✅ 순환 참조 방지
    private Recipe recipe;

    @Column(nullable = false, length = 255)
    private String message; // 알림 메시지

    @Column(nullable = false)
    private boolean readYn = false; // 읽음 여부

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
}
