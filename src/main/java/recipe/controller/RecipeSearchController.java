package recipe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import recipe.dto.RecipeSearchDto;
import recipe.service.RecipeService;

@Slf4j 
@RestController
@RequestMapping("/api/recipesearch")
@Tag(name ="레시피 검색", description = "레시피 검색 관련 API 목록")
public class RecipeSearchController {
	
	@Autowired
	private RecipeService recipeService;
	
	@GetMapping
	@Operation(summary = "레피시 검색", description = "필터링 없이 레시피 검색 ")
    public ResponseEntity<List<RecipeSearchDto>> searchRecipes(@RequestParam("keyword") String keyword) {
        List<RecipeSearchDto> recipes = recipeService.searchRecipes(keyword);
        return ResponseEntity.ok(recipes);
    }
	
	// 제목으로 검색
	@GetMapping(value = "/title")
	@Operation(summary = "레피시 제목 검색", description = "요리명으로 레시피 검색 기능")
    public ResponseEntity<List<RecipeSearchDto>> searchByTitle(@RequestParam("title") String title) {
        List<RecipeSearchDto> recipes = recipeService.searchRecipesByTitle(title);
        return ResponseEntity.ok(recipes);
    }
    
    // 재료로 검색
    @GetMapping("/ingredients")
	@Operation(summary = "레피시 재료 검색", description = "재료로 레시피 검색 기능")
    public ResponseEntity<List<RecipeSearchDto>> searchByIngredient(@RequestParam("ingredient") String ingredient) {
    	List<RecipeSearchDto> recipes = recipeService.searchRecipesByIngredient(ingredient);
        return ResponseEntity.ok(recipes);
    }
    
    // 카테고리(밥,국, 후식)으로 검색
    @GetMapping("/category")
	@Operation(summary = "카테고리 필터링 검색", description = "카테고리 필터링(밥,국,후식)등으로 레시피 검색 기능")
    public ResponseEntity<List<RecipeSearchDto>> searchBCategory(@RequestParam("category") String category, @RequestParam("keyword") String keyword) {
    	List<RecipeSearchDto> recipes = recipeService.searchRecipesByCategory(category,keyword);
        return ResponseEntity.ok(recipes);
    }
    
    // 카테고리 필터링 후 요리 이름으로 검색
    @GetMapping("/categorytitle")
	@Operation(summary = "카테고리&요리명 필터링 검색", description = "카테고리 필터링(밥,국,후식)후 요리명으로 레시피 검색 기능")
    public ResponseEntity<List<RecipeSearchDto>> searchBCategorAndTitle(@RequestParam("category") String category, @RequestParam("keyword") String keyword) {
    	List<RecipeSearchDto> recipes = recipeService.searchRecipesByCategoryaAndTitle(category,keyword);
        return ResponseEntity.ok(recipes);
    }
    
    // 카테고리 필터링  재료로  검색
    @GetMapping("/categoryingredient")
	@Operation(summary = "카테고리&재료  필터링 검색", description = "카테고리 필터링(밥,국,후식)후 재료로 레시피 검색 기능")
    public ResponseEntity<List<RecipeSearchDto>> searchBCategorAnIngredient(@RequestParam("category") String category, @RequestParam("keyword") String keyword) {
    	List<RecipeSearchDto> recipes = recipeService.searchRecipesByCategoryaAnIngredient(category,keyword);
        return ResponseEntity.ok(recipes);
    }

}
