package recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipe.entity.Recipe;
import recipe.entity.RecipeSteps;

import java.util.List;

public interface RecipeStepRepository extends JpaRepository<RecipeSteps, Long> {
    List<RecipeSteps> findByRecipeId(Long recipeId);

    void deleteAllByRecipe(Recipe recipe);
}
