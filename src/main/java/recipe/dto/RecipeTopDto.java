package recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor  // 모든 필드를 포함한 생성자 자동 생성
@NoArgsConstructor   // 기본 생성자 자동 생성
public class RecipeTopDto {
    private Long id;         // 레시피 아이디
    private String title;    // 레시피 제목
    private String imageLarge; // 큰 이미지 URL
}
