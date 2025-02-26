package recipe.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import recipe.dto.*;
import recipe.entity.Recipe;
import recipe.entity.RecipeSteps;
import recipe.entity.User;
import recipe.service.RecipeService;
import recipe.service.RecipeServiceImpl;
import recipe.service.UserService;
import recipe.service.UserServiceImpl;

@RestController
@RequestMapping("/api/recipes")
@Tag(name = "Recipe API", description = "레시피 관련 API 목록")
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;

    public RecipeController(RecipeServiceImpl recipeService, UserServiceImpl userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @PostMapping("/{userId}")
    @Operation(summary = "레시피 생성", description = "새로운 레시피를 생성합니다. 사용자의 ID와 함께 레시피 정보를 전달합니다.")
    public Recipe createRecipe(@RequestBody RecipeWithStepsDto recipeWithStepsDTO,
    		@Parameter(description = "사용자의 ID", required = true, example = "1") @PathVariable("userId") Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return recipeService.saveRecipe(recipeWithStepsDTO.getRecipe(), user);
    }

    @PutMapping("/recipes/{id}")
    @Operation(summary = "레시피 수정", description = "기존 레시피를 수정합니다. 사용자의 ID와 함께 수정할 내용을 전달합니다.")
    public ResponseEntity<Recipe> updateRecipe(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestBody Map<String, Object> request) {

        User user = userService.getUserById(userId);

        ObjectMapper objectMapper = new ObjectMapper();
        Recipe updatedRecipe = objectMapper.convertValue(request.get("recipe"), Recipe.class);

        List<RecipeSteps> steps = objectMapper.convertValue(request.get("recipeSteps"),
                new TypeReference<List<RecipeSteps>>() {});

        Recipe savedRecipe = recipeService.updateRecipe(id, updatedRecipe, user, steps);
        return ResponseEntity.ok(savedRecipe);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "레시피 삭제", description = "레시피를 삭제합니다. 사용자가 본인의 레시피만 삭제할 수 있으며, 관리자는 모든 레시피를 삭제할 수 있습니다.")
    public String deleteRecipe(@PathVariable Long id, @RequestParam Long userId) {
        User user = userService.getUserById(userId);
        recipeService.softDeleteRecipe(id, user);
        return "레시피가 삭제되었습니다.";
    }

    @GetMapping
    @Operation(summary = "일반 사용자 레시피 조회", description = "삭제되지 않은 레시피 목록을 조회합니다. (id, 제목만 반환)")
    public ResponseEntity<List<RecipeSummaryDto>> getAllRecipes() {
        List<RecipeSummaryDto> recipes = recipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/admin")
    @Operation(summary = "관리자 레시피 조회", description = "삭제된 레시피를 포함한 전체 목록을 조회합니다. (id, 제목만 반환)")
    public ResponseEntity<List<RecipeSummaryDto>> getAllRecipesForAdmin(@RequestParam Long userId) {
        User user = userService.getUserById(userId);

        if (user == null || !user.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<RecipeSummaryDto> recipes = recipeService.getAllRecipesForAdmin();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "레시피 상세 조회", description = "레시피의 상세 정보를 조회하며 조회수를 증가시킵니다.")
    public RecipeDetailDto getRecipeDetail(@PathVariable Long id, @RequestParam Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Recipe recipe = recipeService.getRecipeByIdWithViewCount(id);

        if (recipe.isDeletedYn() && !user.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe has been deleted");
        }

        Hibernate.initialize(recipe.getRecipeSteps());

        return convertToRecipeDetailDto(recipe);
    }

    public RecipeDetailDto convertToRecipeDetailDto(Recipe recipe) {
        RecipeDetailDto dto = new RecipeDetailDto();
        dto.setRecipeId(recipe.getId());
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

    @GetMapping("/{id}/views")
    @Operation(summary = "레시피 조회수 확인", description = "특정 레시피의 조회수를 반환합니다.")
    public ResponseEntity<Integer> getRecipeViewCount(@PathVariable Long id) {
        int viewCount = recipeService.getRecipeViewCount(id);
        return ResponseEntity.ok(viewCount);
    }

    // 좋아요 수가 많은 상위 3개의 레시피를 조회하여 반환
    @GetMapping("/topliked")
    public ResponseEntity<List<RecipeTopDto>> getTopLikedRecipes() {
        // 상위 3개의 레시피를 조회
        List<RecipeTopDto> topRecipes = recipeService.getTopRecipesByFavoriteCount();
        return ResponseEntity.ok(topRecipes); // 성공적인 응답 반환
    }
}
