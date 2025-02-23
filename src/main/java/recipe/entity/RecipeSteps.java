package recipe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recipesteps")  // 🔥 테이블 이름 정확히 매칭
@Getter
@Setter
@NoArgsConstructor
public class RecipeSteps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepId;

    @ManyToOne(fetch = FetchType.LAZY)  // 🔥 지연 로딩으로 최적화
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(nullable = false)
    private int stepNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    // 🔥 Recipe 설정 메서드 추가
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        if (!recipe.getRecipeSteps().contains(this)) {
            recipe.getRecipeSteps().add(this);
        }
    }
}
