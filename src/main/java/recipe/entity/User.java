package recipe.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String passwordHash;
    
    @Column(nullable = false, unique = true, length = 255)
    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    
    // 활동 정지 여부: false (기본값, 활성) / true (정지)
    @Column(nullable = false)
    private boolean suspended = false;
    
    // 프로필 관련 필드 추가 
    // 프로필 사진 URL
    @Column(length = 500)
    private String profileImageUrl;

    // 간단한 소개글
    @Column(length = 255)
    private String description;

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }
    
    public boolean isSuspended() {
        return suspended;
    }
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // JSON 직렬화 허용 (Recipe에서 @JsonBackReference 사용)
    private List<Recipe> recipes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // JSON 직렬화 허용 (Comment에서 @JsonBackReference 사용)
    private List<Comment> comments;
}