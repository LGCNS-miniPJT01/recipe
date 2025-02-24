package recipe.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import recipe.dto.RecipeSummaryDto;
import recipe.entity.Recipe;
import recipe.entity.RecipeSteps;
import recipe.entity.User;
import recipe.repository.FavoriteRepository;
import recipe.repository.RecipeRepository;
import recipe.repository.RecipeStepRepository;

@Service
public class RecipeServiceImpl implements RecipeService {

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private RecipeStepRepository recipeStepRepository;
	
	@Autowired
	private FavoriteRepository favoriteRepository;

	// 레시피 저장
	@Override
	@Transactional
	public Recipe saveRecipe(Recipe newRecipe, User user) {
		if (user == null) {
			throw new RuntimeException("로그인한 사용자 정보가 필요합니다.");
		}

		if (newRecipe.getTitle() == null || newRecipe.getTitle().isEmpty()) {
			throw new IllegalArgumentException("레시피 제목은 필수 입력 항목입니다.");
		}

		List<RecipeSteps> steps = newRecipe.getRecipeSteps(); // JSON의 recipeSteps 사용

		if (steps == null || steps.isEmpty()) {
			throw new IllegalArgumentException("레시피 단계가 필요합니다.");
		}

		newRecipe.setUser(user);
		newRecipe.setCreatedAt(new Date());
		newRecipe.setUpdatedAt(new Date());
		newRecipe.setDeletedYn(false);

		// 레시피와 스텝 연결
		for (RecipeSteps step : steps) {
			step.setRecipe(newRecipe);
		}

		// 레시피 저장 (cascade로 steps도 저장됨)
		return recipeRepository.save(newRecipe);
	}

	// 레시피 수정
	@Override
	@Transactional
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

		recipeStepRepository.deleteAllByRecipe(recipe);

		if (steps != null && !steps.isEmpty()) {
			int stepNumber = 1;
			for (RecipeSteps step : steps) {
				step.setRecipe(recipe);
				step.setStepNumber(stepNumber++);
				recipeStepRepository.save(step);
			}
		}

		return recipeRepository.save(recipe);
	}


	// 레시피 삭제
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


	// 일반 사용자용 레시피 조회 (deletedYn = false)
	@Override
	public List<RecipeSummaryDto> getAllRecipes() {
		List<Recipe> recipes = recipeRepository.findByDeletedYnFalse();
		return recipes.stream()
				.map(recipe -> new RecipeSummaryDto(recipe.getRecipeId(), recipe.getTitle()))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Recipe getRecipeByIdWithViewCount(Long recipeId) {
		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

		// 조회수 증가
		recipe.setViewCount(recipe.getViewCount() + 1);
		return recipe;
	}

	// 관리자용 전체 레시피 조회 (삭제된 레시피 포함)
	@Override
	public List<RecipeSummaryDto> getAllRecipesForAdmin() {
		List<Recipe> recipes = recipeRepository.findAll();
		return recipes.stream()
				.map(recipe -> new RecipeSummaryDto(recipe.getRecipeId(), recipe.getTitle()))
				.collect(Collectors.toList());
	}

	// 상세 내용 조회
	@Override
	public Optional<Recipe> getRecipeById(Long recipeId) {
		return recipeRepository.findById(recipeId);
	}

	// 조회수
	public int getRecipeViewCount(Long recipeId) {
		Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
		return recipe.getViewCount();
	}

	@Override
	//좋아요 수(내림차순)을 기준으로 레시피 받기
	public List<Recipe> getTopRecipesByFavoriteCount() {
        List<Object[]> result = favoriteRepository.findTopRecipesByFavoriteCount();
        
        // 결과를 Recipe 리스트로 변환
        return result.stream()
                     .map(r -> (Recipe) r[0])  // 첫 번째 요소는 Recipe 객체
                     .collect(Collectors.toList());
    }
}