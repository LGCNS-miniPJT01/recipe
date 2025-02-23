package recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import recipe.entity.Recipe;
import recipe.entity.RecipeSteps;
import recipe.entity.User;
import recipe.repository.RecipeRepository;
import recipe.repository.RecipeStepRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeServiceImpl implements RecipeService {

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private RecipeStepRepository recipeStepRepository;

	@Override
	@Transactional
	public Recipe saveRecipe(Recipe newRecipe, User user, List<RecipeSteps> steps) {

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

		// 레시피 저장
		Recipe savedRecipe = recipeRepository.save(newRecipe);

		// steps가 null인 경우 예외 처리
		if (steps == null) {
			throw new IllegalArgumentException("레시피 단계가 필요합니다.");
		}

		// 레시피 스텝 저장
		for (RecipeSteps step : steps) {
			step.setRecipe(savedRecipe); // 레시피와 스텝 연결
			recipeStepRepository.save(step);
		}

		return savedRecipe;
	}


	// 레시피 수정
	@Override
	public Recipe updateRecipe(Long recipeId, Recipe updatedRecipe, User user, List<RecipeSteps> steps) {
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

		// 기존 레시피 스텝 삭제 (새로운 스텝만 남기기)
		recipeStepRepository.deleteAllByRecipe(recipe);

		// 새 스텝들 저장
		int stepNumber = 1;  // 스텝 번호 초기화
		for (RecipeSteps step : steps) {
			step.setRecipe(recipe); // 레시피와 스텝 연결
			step.setStepNumber(stepNumber);  // 스텝 번호 설정
			recipeStepRepository.save(step);
			stepNumber++;  // 스텝 번호 증가
		}

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

		// 일반 사용자는 본인 레시피만 삭제 가능, 관리자는 모두 삭제 가능
		if (!user.isAdmin() && !recipe.getUser().getUserId().equals(user.getUserId())) {
			throw new RuntimeException("본인이 작성한 레시피만 삭제할 수 있습니다.");
		}

		// deletedYn = true로 변경하여 논리적 삭제
		recipe.setDeletedYn(true);
		recipeRepository.save(recipe);
	}

	// 일반 사용자의 레시피 조회
	@Override
	public List<Recipe> getAllRecipes() {
		List<Recipe> recipes = recipeRepository.findByDeletedYnFalse();
		for (Recipe recipe : recipes) {
			// 레시피와 연관된 스텝을 함께 반환
			recipe.setRecipeSteps(recipeStepRepository.findByRecipeId(recipe.getRecipeId()));
		}
		return recipes;
	}

	// 관리자 사용자의 레시피 조회
	@Override
	public List<Recipe> getAllRecipesForAdmin() {
		List<Recipe> recipes = recipeRepository.findAll();
		for (Recipe recipe : recipes) {
			// 레시피와 연관된 스텝을 함께 반환
			recipe.setRecipeSteps(recipeStepRepository.findByRecipeId(recipe.getRecipeId()));
		}
		return recipes;
	}
}
