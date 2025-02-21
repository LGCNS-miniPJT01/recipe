package recipe.service;

import java.util.List;

import recipe.entity.Recipe;
import recipe.entity.User;

public interface RecipeService {	
	Recipe saveRecipe(Recipe recipe);
	List<Recipe> getAllRecipes();
	void softDeleteRecipe(Long recipId, User user);
	List<Recipe> getAllRecipesForAdmin();
}