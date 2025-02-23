package recipe.dto;

import lombok.Getter;
import lombok.Setter;
import recipe.entity.Recipe;
import java.util.List;

@Getter
@Setter
public class RecipeWithStepsDto {
    private Recipe recipe;
    private List<RecipeStepDto> steps; // <-- 여기 수정 (RecipeSteps → RecipeStepDto)
}
