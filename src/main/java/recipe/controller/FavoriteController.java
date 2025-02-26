package recipe.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import recipe.entity.User;
import recipe.service.FavoriteService;
import recipe.service.UserService;

@RestController
@RequestMapping("/api/favorites")
@Tag(name = "Favorite API", description = "레시피 좋아요 관련 API")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final UserService userService;

    @PostMapping("/{id}")
    @Operation(summary = "좋아요 추가", description = "레시피에 좋아요를 추가합니다.")
    public ResponseEntity<Map<String, String>> addFavorite(@PathVariable Long recipeId, @RequestParam Long userId) {
        favoriteService.addFavorite(recipeId, userId);
        return ResponseEntity.ok(Map.of("message", "좋아요 추가 완료"));
    }

    @DeleteMapping("/{recipeId}")
    @Operation(summary = "좋아요 취소", description = "레시피의 좋아요를 취소합니다.")
    public ResponseEntity<Map<String, String>> removeFavorite(@PathVariable Long recipeId, @RequestParam Long userId) {
        favoriteService.removeFavorite(recipeId, userId);
        return ResponseEntity.ok(Map.of("message", "좋아요 취소 완료"));
    }

    @GetMapping("/count/{recipeId}")
    @Operation(summary = "좋아요 개수 조회", description = "레시피의 좋아요 개수를 조회합니다.")
    public ResponseEntity<Map<String, Integer>> getFavoriteCount(@PathVariable Long recipeId) {
        int count = favoriteService.getFavoriteCount(recipeId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/users/{recipeId}")
    @Operation(summary = "좋아요 누른 사용자 목록 조회", description = "레시피를 좋아요한 사용자 목록을 반환합니다.")
    public ResponseEntity<List<Map<String, Object>>> getFavoriteUsers(@PathVariable Long recipeId) {
        List<User> users = favoriteService.getFavoriteUsers(recipeId);

        List<Map<String, Object>> favoriteList = users.stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", user.getUserId());
                    map.put("userName", user.getUsername());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(favoriteList);
    }
}
