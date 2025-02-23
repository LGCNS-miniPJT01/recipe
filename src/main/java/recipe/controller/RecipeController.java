package recipe.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipe.dto.RecipeDetailDto;
import recipe.dto.RecipeStepDto;
import recipe.dto.RecipeWithStepsDto;
import recipe.entity.Recipe;
import recipe.entity.RecipeSteps;
import recipe.entity.User;
import recipe.service.RecipeServiceImpl;
import recipe.service.UserServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // ✅ 일반 사용자의 레시피 조회 (삭제되지 않은 레시피만)
    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        List<Recipe> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }

    // ✅ 관리자용 전체 레시피 조회 (삭제된 레시피 포함)
    @GetMapping("/admin")
    public ResponseEntity<List<Recipe>> getAllRecipesForAdmin(@RequestParam Long userId) {
        User user = userService.getUserById(userId);

        // 관리자 권한 확인
        if (!user.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Recipe> recipes = recipeService.getAllRecipesForAdmin();
        return ResponseEntity.ok(recipes);
    }

    // 특정 레시피 상세 조회 (일반 사용자 & 관리자)
    @GetMapping("/{recipeId}")
    public RecipeDetailDto getRecipeDetail(@PathVariable Long recipeId, @RequestParam Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Recipe recipe = recipeService.getRecipeById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        if (recipe.isDeletedYn() && !user.isAdmin()) {  // 관리자가 아니면 삭제된 레시피는 못 봄
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe has been deleted");
        }

        return convertToRecipeDetailDto(recipe);
    }


    public RecipeDetailDto convertToRecipeDetailDto(Recipe recipe) {
        RecipeDetailDto dto = new RecipeDetailDto();
        dto.setRecipeId(recipe.getRecipeId());
        dto.setTitle(recipe.getTitle());
        dto.setCookingMethod(recipe.getCookingMethod());
        dto.setCategory(recipe.getCategory());
        dto.setWeight(recipe.getWeight());
        dto.setEnergy(recipe.getEnergy());
        dto.setCarbohydrate(recipe.getCarbohydrate());
        dto.setProtein(recipe.getProtein());
        dto.setFat(recipe.getFat());
        dto.setSodium(recipe.getSodium());
        dto.setHashTag(recipe.getHashTag());
        dto.setImageSmall(recipe.getImageSmall());
        dto.setImageLarge(recipe.getImageLarge());
        dto.setIngredients(recipe.getIngredients());
        dto.setTip(recipe.getTip());

        // RecipeSteps -> RecipeStepDto 변환
        dto.setSteps(recipe.getRecipeSteps().stream().map(step -> {
            RecipeStepDto stepDto = new RecipeStepDto();
            stepDto.setStepId(step.getStepId());
            stepDto.setStepNumber(step.getStepNumber());
            stepDto.setDescription(step.getDescription());
            stepDto.setImageUrl(step.getImageUrl());
            return stepDto;
        }).collect(Collectors.toList()));

        return dto;
    }



}
