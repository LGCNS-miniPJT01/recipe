package recipe.service;

import recipe.entity.Recipe;
import recipe.entity.User;

import java.util.List;

public interface RecipeService {
	// 레시피 생성
	Recipe saveRecipe(Recipe recipe, User user);

	//레시피 수정
	Recipe updateRecipe(Long recipeId, Recipe updatedRecipe, User user);

	//레시피 삭제
	void softDeleteRecipe(Long recipId, User user);

	//레시피 조회
	List<Recipe> getAllRecipes();

	List<Recipe> getAllRecipesForAdmin();
}