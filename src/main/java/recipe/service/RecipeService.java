package recipe.service;

import java.util.List;
import java.util.Optional;

import recipe.dto.RecipeSearchDto;
import recipe.dto.RecipeSummaryDto;
import recipe.entity.Recipe;
import recipe.entity.RecipeSteps;
import recipe.entity.User;

public interface RecipeService {

	// 레시피 생성
	Recipe saveRecipe(Recipe newRecipe, User user);

	// 레시피 수정
	Recipe updateRecipe(Long recipeId, Recipe updatedRecipe, User user, List<RecipeSteps> steps);

	// 레시피 논리 삭제
	void softDeleteRecipe(Long recipeId, User user);

	// 일반 사용자의 레시피 조회
	List<RecipeSummaryDto> getAllRecipes();

	// 관리자 사용자의 모든 레시피 조회
	List<RecipeSummaryDto> getAllRecipesForAdmin();

	// 리시피 아이디로 레시피 조회
	Optional<Recipe> getRecipeById(Long recipeId);

	//레시피의 조회수 확인
	Recipe getRecipeByIdWithViewCount(Long recipeId);
	
	//좋아요 수(내림차순)으로 레시피 조회
	List<Recipe> getTopRecipesByFavoriteCount();
	
	// 레시피 검색 기능 제목,재료
	List<RecipeSearchDto> searchRecipesByTitle(String title);
	List<RecipeSearchDto> searchRecipesByIngredient(String ingredient);
	
	// 필터링 선택 없이 전체 검색
	List<RecipeSearchDto> searchRecipes(String keyword);
	
	// 필터링(밥,국,후식 등) 선택 후 전체 검색
	List<RecipeSearchDto> searchRecipesByCategory(String category, String keyword);
	
	// 카테고리 필터링 후 요리 이름으로 검색
	List<RecipeSearchDto> searchRecipesByCategoryaAndTitle(String category, String keyword);
	
	// 카테고리 필터링 후 재료로  검색
	List<RecipeSearchDto> searchRecipesByCategoryaAnIngredient(String category, String keyword);
	
}
