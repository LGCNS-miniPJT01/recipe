package recipe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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
    
    @Column(nullable = false, length = 255)
    private String ingredients;
    
    @Column(columnDefinition = "TEXT")
    private String tip;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();
    
    @Column(nullable = false)
    private boolean deletedYn = false;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeSteps> recipeSteps;

    @Column(nullable = false)
    private int viewCount = 0;
}