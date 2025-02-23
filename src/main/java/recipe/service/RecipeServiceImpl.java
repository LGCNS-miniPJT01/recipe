package recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import recipe.entity.Recipe;
import recipe.entity.User;
import recipe.repository.RecipeRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {
	@Autowired
	private RecipeRepository recipeRepository;

	// 레시피 생성
	@Override
	@Transactional
	public Recipe saveRecipe(Recipe newRecipe, User user) {

		if (user == null) {
			throw new RuntimeException("로그인한 사용자 정보가 필요합니다.");
		}

		if (newRecipe.getTitle() == null || newRecipe.getTitle().isEmpty()) {
			throw new IllegalArgumentException("레시피 제목은 필수 입력 항목입니다.");
		}

		newRecipe.setUser(user);  // 로그인한 사용자 설정
		newRecipe.setCreatedAt(new Date());
		newRecipe.setUpdatedAt(new Date());
		newRecipe.setDeletedYn(false);
		return recipeRepository.save(newRecipe);
	}

	// 레시피 수정
	@Override
	public Recipe updateRecipe(Long recipeId, Recipe updatedRecipe, User user) {
		Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);

		if (optionalRecipe.isEmpty()) {
			throw new RuntimeException("레시피를 찾을 수 없습니다.");
		}

		Recipe recipe = optionalRecipe.get();

		// 본인이 작성한 레시피만 수정 가능 (관리자는 예외)
		if (!user.isAdmin() && !recipe.getUser().getUserId().equals(user.getUserId())) {
			throw new RuntimeException("본인이 작성한 레시피만 수정할 수 있습니다.");
		}

		// 기존 값 업데이트
		recipe.setTitle(updatedRecipe.getTitle());
		recipe.setCookingMethod(updatedRecipe.getCookingMethod());
		recipe.setCategory(updatedRecipe.getCategory());
		recipe.setWeight(updatedRecipe.getWeight());
		recipe.setEnergy(updatedRecipe.getEnergy());
		recipe.setCarbohydrate(updatedRecipe.getCarbohydrate());
		recipe.setProtein(updatedRecipe.getProtein());
		recipe.setFat(updatedRecipe.getFat());
		recipe.setSodium(updatedRecipe.getSodium());
		recipe.setHashTag(updatedRecipe.getHashTag());
		recipe.setImageSmall(updatedRecipe.getImageSmall());
		recipe.setImageLarge(updatedRecipe.getImageLarge());
		recipe.setIngredients(updatedRecipe.getIngredients());
		recipe.setTip(updatedRecipe.getTip());

		recipe.setUpdatedAt(new Date());

		return recipeRepository.save(recipe);
	}

	// 삭제
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