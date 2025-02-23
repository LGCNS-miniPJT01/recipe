package recipe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipe.entity.Scrap;
import recipe.service.ScrapService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scrap")
@Tag(name = "Scrap API", description = "레시피 스크랩 관련 API")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping("/{recipeId}")
    @Operation(summary = "스크랩 추가", description = "레시피를 스크랩합니다.")
    public ResponseEntity<Map<String, String>> addScrap(@PathVariable Long recipeId, @RequestParam Long userId) {
        scrapService.addScrap(userId, recipeId);
        return ResponseEntity.ok(Map.of("message", "스크랩 추가 완료"));
    }

    @DeleteMapping("/{recipeId}")
    @Operation(summary = "스크랩 취소", description = "스크랩한 레시피를 삭제합니다.")
    public ResponseEntity<Map<String, String>> removeScrap(@PathVariable Long recipeId, @RequestParam Long userId) {
        scrapService.removeScrap(userId, recipeId);
        return ResponseEntity.ok(Map.of("message", "스크랩 취소 완료"));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "사용자의 스크랩한 레시피 목록 조회", description = "사용자가 스크랩한 레시피 목록을 반환합니다.")
    public ResponseEntity<List<Map<String, Object>>> getScrappedRecipes(@PathVariable Long userId) {
        List<Scrap> scraps = scrapService.getScrappedRecipes(userId);

        List<Map<String, Object>> scrappedRecipes = scraps.stream()
                .map(scrap -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("scrapId", scrap.getScrapId());
                    map.put("recipeId", scrap.getRecipe().getRecipeId());
                    map.put("recipeTitle", scrap.getRecipe().getTitle());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(scrappedRecipes);
    }

}
