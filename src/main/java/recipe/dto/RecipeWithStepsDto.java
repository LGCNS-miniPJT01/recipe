package recipe.dto;

import lombok.Getter;
import lombok.Setter;
import recipe.entity.Recipe;
import recipe.entity.RecipeSteps;

import java.util.List;

@Getter
@Setter
public class RecipeWithStepsDto {
    private Recipe recipe;
    private List<RecipeSteps> steps;
}