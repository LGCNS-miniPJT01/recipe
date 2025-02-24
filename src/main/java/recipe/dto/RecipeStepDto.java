package recipe.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RecipeStepDto {

    private Long stepId;
    private int stepNumber; // 단계 번호
    private String description; // 설명
    private String imageUrl; // 이미지 URL
}
