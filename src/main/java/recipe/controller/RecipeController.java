package recipe.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipe.dto.RecipeWithStepsDto;
import recipe.entity.Recipe;
import recipe.entity.RecipeSteps;
import recipe.entity.User;
import recipe.service.RecipeServiceImpl;
import recipe.service.UserServiceImpl;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
@Tag(name = "Recipe API", description = "레시피 관련 API 목록")
public class RecipeController {

    private final RecipeServiceImpl recipeService;
    private final UserServiceImpl userService;

    public RecipeController(RecipeServiceImpl recipeService, UserServiceImpl userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @PostMapping("/{userId}")
    @Operation(summary = "레시피 생성", description = "새로운 레시피를 생성합니다. 사용자의 ID와 함께 레시피 정보를 전달합니다.")
    public Recipe createRecipe(@RequestBody RecipeWithStepsDto recipeWithStepsDTO, @PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return recipeService.saveRecipe(recipeWithStepsDTO.getRecipe(), user);
    }

    @PutMapping("/recipes/{recipeId}")
    public ResponseEntity<Recipe> updateRecipe(
            @PathVariable Long recipeId,
            @RequestParam Long userId,
            @RequestBody Map<String, Object> request) {

        User user = userService.getUserById(userId);

        ObjectMapper objectMapper = new ObjectMapper();
        Recipe updatedRecipe = objectMapper.convertValue(request.get("recipe"), Recipe.class);

        List<RecipeSteps> steps = objectMapper.convertValue(request.get("recipeSteps"),
                new TypeReference<List<RecipeSteps>>() {});

        Recipe savedRecipe = recipeService.updateRecipe(recipeId, updatedRecipe, user, steps);
        return ResponseEntity.ok(savedRecipe);
    }


    @DeleteMapping("/{recipeId}")
    @Operation(summary = "레시피 삭제", description = "레시피를 삭제합니다. 사용자가 본인의 레시피만 삭제할 수 있습니다. 관리자는 모든 레시피 삭제 가능.")
    public String deleteRecipe(@PathVariable Long recipeId, @RequestParam Long userId) {
        User user = userService.getUserById(userId);
        recipeService.softDeleteRecipe(recipeId, user);
        return "레시피가 삭제되었습니다.";
    }
}
