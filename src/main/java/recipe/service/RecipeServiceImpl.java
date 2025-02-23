package recipe.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import recipe.entity.Recipe;
import recipe.entity.User;
import recipe.repository.RecipeRepository;

@Service
public class RecipeServiceImpl implements RecipeService {
	@Autowired
	private RecipeRepository recipeRepository;

	@Override
	@Transactional
	public void softDeleteRecipe(Long recipeId, User user) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        
        if (optionalRecipe.isEmpty()) {
            throw new RuntimeException("레시피를 찾을 수 없습니다.");
        }

        Recipe recipe = optionalRecipe.get();

        //일반 사용자는 본인 레시피만 삭제 가능, 관리자는 모두 삭제 가능
        if (!user.isAdmin() && !recipe.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("본인이 작성한 레시피만 삭제할 수 있습니다.");
        }
        
        //deletedYn = true 로 변경하여 논리적 삭제
        recipe.setDeletedYn(true);
        recipeRepository.save(recipe);
	}

	@Override
	@Transactional
	public Recipe saveRecipe(Recipe recipe) {
		return recipeRepository.save(recipe);
	}

	//일반 사용자의 레시피 조회
	@Override
	public List<Recipe> getAllRecipes() {
		return recipeRepository.findByDeletedYnFalse();
	}
	
	//관리자 사용자의 레시피 조회
	@Override
	public List<Recipe> getAllRecipesForAdmin() {
        return recipeRepository.findAll();
    }
	


}