package recipe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipe.service.ScrapService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scrap")
@Tag(name = "Scrap API", description = "레시피 스크랩 관련 API")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;


    @PostMapping("/{id}")
    @Operation(summary = "스크랩 추가", description = "레시피를 스크랩합니다.")
    public ResponseEntity<Map<String, String>> addScrap(@PathVariable Long id, @RequestParam Long userId) {
        scrapService.addScrap(userId, id);
        return ResponseEntity.ok(Map.of("message", "스크랩 추가 완료"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "스크랩 취소", description = "스크랩한 레시피를 삭제합니다.")
    public ResponseEntity<Map<String, String>> removeScrap(@PathVariable Long id, @RequestParam Long userId) {
        scrapService.removeScrap(userId, id);
        return ResponseEntity.ok(Map.of("message", "스크랩 취소 완료"));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "개인 레시피 목록 조회", description = "유저 ID를 입력하면 스크랩한 레시피 ID 목록을 반환합니다.")
    public ResponseEntity<List<Long>> getScrapList(@PathVariable Long userId) {
        List<Long> ids = scrapService.getScrapList(userId);
        return ResponseEntity.ok(ids);
    }

}
