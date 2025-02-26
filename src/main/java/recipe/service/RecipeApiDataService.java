package recipe.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import recipe.dto.RecipeApiDto;
import recipe.entity.Recipe;
import recipe.entity.RecipeSteps;
import recipe.entity.User;
import recipe.repository.RecipeRepository;
import recipe.repository.RecipeStepRepository;
import recipe.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeApiDataService {

    private final RecipeRepository recipeRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public void saveRecipes(RecipeApiDto response) {
        for (var recipeItem : response.getCookRcp01().getRow()) {
            try {
                saveSingleRecipe(recipeItem);
            } catch (Exception e) {
                log.error("🚨 레시피 저장 실패 (ID={}): {}", recipeItem.getRecipeId(), e.getMessage());
            }
        }
        log.info("✅ {}개의 레시피 데이터가 DB에 저장됨.", response.getCookRcp01().getRow().size());
    }

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSingleRecipe(RecipeApiDto.RecipeItem recipeItem) {
        Long recipeId = Long.parseLong(recipeItem.getRecipeId());
        Optional<Recipe> existingRecipe = recipeRepository.findByIdForUpdate(recipeId);

        User recipeUser = userRepository.findByuserId(1L);

        Recipe recipe = existingRecipe.orElseGet(Recipe::new);
        recipe.setId(recipeId);
        recipe.setTitle(recipeItem.getTitle());
        recipe.setCookingMethod(recipeItem.getCookingMethod());
        recipe.setCategory(recipeItem.getCategory());
        recipe.setWeight(recipeItem.getWeight());
        recipe.setEnergy(parseOrDefault(recipeItem.getEnergy(), 0));
        recipe.setCarbohydrate(parseOrDefault(recipeItem.getCarbohydrate(), 0.0f));
        recipe.setProtein(parseOrDefault(recipeItem.getProtein(), 0.0f));
        recipe.setFat(parseOrDefault(recipeItem.getFat(), 0.0f));
        recipe.setSodium(parseOrDefault(recipeItem.getSodium(), 0.0f));
        recipe.setHashTag(recipeItem.getHashTag());
        recipe.setImageLarge(recipeItem.getImageLarge());
        recipe.setImageSmall(recipeItem.getImageSmall());
        recipe.setIngredients(recipeItem.getIngredients());
        recipe.setTip(recipeItem.getTip());
        recipe.setUser(recipeUser);

        log.info(">>>>> 레시피 저장 (ID={})", recipeId);
        recipeRepository.save(recipe);

        // 레시피 스텝 저장 (단계 정보 있는 경우)
        if (recipeItem.getManualSteps() != null && !recipeItem.getManualSteps().isEmpty()) {
            List<RecipeSteps> steps = extractRecipeSteps(recipeItem.getManualSteps(), recipeItem.getManualImages(), recipe);
            recipeStepRepository.saveAll(steps);
        }
    }

    private List<RecipeSteps> extractRecipeSteps(Map<String, String> manualSteps, Map<String, String> manualImages, Recipe recipe) {
        List<RecipeSteps> steps = new ArrayList<>();
        
        if (manualSteps == null || manualSteps.isEmpty()) {
            log.warn("⚠️ manualSteps 데이터 없음 (Recipe ID={})", recipe.getRecipeId());
            return steps;
        }

        for (int i = 1; i <= 20; i++) {
            String stepKey = String.format("MANUAL%02d", i);
            String imageKey = String.format("MANUAL_IMG%02d", i);

            String description = manualSteps.get(stepKey);
            String imageUrl = (manualImages != null) ? manualImages.get(imageKey) : null;

            if (description == null || description.trim().isEmpty()) {
                log.info("📌 Step {}의 설명 없음 (Recipe ID={}), 저장 안함", i, recipe.getRecipeId());
                continue;
            }

            RecipeSteps step = new RecipeSteps();
            step.setRecipe(recipe);
            step.setStepNumber(i);
            step.setDescription(description);
            step.setImageUrl(imageUrl);

            steps.add(step);
        }

        log.info("✅ 총 {}개의 RecipeSteps 저장 (Recipe ID={})", steps.size(), recipe.getRecipeId());
        return steps;
    }

    private int parseOrDefault(String value, int defaultValue) {
        try {
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private float parseOrDefault(String value, float defaultValue) {
        try {
            return value != null ? Float.parseFloat(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
