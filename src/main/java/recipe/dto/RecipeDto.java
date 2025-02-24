package recipe.dto;

import lombok.Data;
import java.util.List;

@Data
public class RecipeDto {

    private Long recipeId;
    private String title; // 레시피 제목
    private List<RecipeStepDto> steps; // 레시피 단계 리스트
}
