package recipe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import recipe.entity.Recipe;
import recipe.entity.User;
import recipe.service.RecipeServiceImpl;
import recipe.service.UserServiceImpl;

import java.util.List;

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

    @PostMapping
    @Operation(summary = "레시피 생성", description = "새로운 레시피를 생성합니다. 사용자의 ID와 함께 레시피 정보를 전달합니다.")
    public Recipe createRecipe(@RequestBody Recipe recipe, @RequestParam Long userId) {
        User user = userService.getUserById(userId);
        return recipeService.saveRecipe(recipe, user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "레시피 수정", description = "기존의 레시피를 수정합니다. 레시피 ID와 수정된 정보를 전달합니다.")
    public Recipe updateRecipe(@PathVariable Long id, @RequestBody Recipe recipe, @RequestParam Long userId) {
        User user = userService.getUserById(userId);
        return recipeService.updateRecipe(id, recipe, user);
    }

    @GetMapping("/list")
    @Operation(summary = "모든 레시피 조회", description = "저장된 모든 레시피를 조회합니다.")
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @DeleteMapping("/{recipeId}")
    @Operation(summary = "레시피 삭제", description = "레시피를 삭제합니다. 사용자가 본인의 레시피만 삭제할 수 있습니다. 관리자는 모든 레시피 삭제 가능.")
    public String deleteRecipe(@PathVariable Long recipeId, @RequestParam Long userId) {
        User user = userService.getUserById(userId);
        recipeService.softDeleteRecipe(recipeId, user);
        return "레시피가 삭제되었습니다.";
    }
}
