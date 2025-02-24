package recipe.dto;

import lombok.Data;
import java.util.List;

@Data
public class RecipeDetailDto {
    private Long recipeId;
    private String title;
    private String cookingMethod;
    private String category;
    private String weight;
    private double energy;
    private double carbohydrate;
    private double protein;
    private double fat;
    private double sodium;
    private String hashTag;
    private String imageSmall;
    private String imageLarge;
    private String ingredients;
    private String tip;
    private List<RecipeStepDto> steps; // 엔티티 대신 DTO 사용
}
