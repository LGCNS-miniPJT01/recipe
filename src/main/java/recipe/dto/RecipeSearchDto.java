package recipe.dto;

import lombok.Getter;
import lombok.Setter;

// 레시피 검색 결과 반환 dto
@Getter
@Setter
public class RecipeSearchDto {
	private Long recipeId;
    private Long userId;  
    private String title;
    private String category;
    private String cookingMethod;
    private String imageSmall;
    private String imageLarge;

}
