package recipe.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Recipes")
@Getter
@Setter
@NoArgsConstructor
public class Recipe {
    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference // JSON 직렬화에서 제외 (User에서 @JsonManagedReference 사용)
    private User user;

    @Column(nullable = false, length = 255)
    private String title;
    
    @Column(nullable = false, length = 255)
    private String cookingMethod;
    
    @Column(nullable = false, length = 255)
    private String category;
    
    @Column(nullable = false, length = 255)
    private String weight;
    
    @Column(nullable = false, length = 255)
    private int energy;
    
    @Column(nullable = false, length = 255)
    private float carbohydrate;
    
    @Column(nullable = false, length = 255)
    private float protein;
    
    @Column(nullable = false, length = 255)
    private float fat;
    
    @Column(nullable = false, length = 255)
    private float sodium;
    
    @Column(nullable = false, length = 255)
    private String hashTag;
    
    @Column(nullable = false, length = 255)
    private String imageSmall;
    
    @Column(nullable = false, length = 255)
    private String imageLarge;
    
    @Column(columnDefinition = "TEXT") // 🔥 VARCHAR(255) → TEXT로 변경
    private String ingredients;
    
    @Column(columnDefinition = "TEXT")
    private String tip;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();
    
    @Column(nullable = false)
    private boolean deletedYn = false;

    // recipesteps 자동 저장을 위한 
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore  
    private List<RecipeSteps> recipeSteps;

    @Column(nullable = false)
    private int viewCount = 0;

    private int favoriteCount;
}