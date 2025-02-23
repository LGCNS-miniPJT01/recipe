package recipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import recipe.entity.Recipe;
import recipe.entity.Scrap;
import recipe.entity.User;
import recipe.repository.RecipeRepository;
import recipe.repository.ScrapRepository;
import recipe.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    @Transactional
    @Override
    public void addScrap(Long userId, Long recipeId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new IllegalArgumentException("Recipe not found"));

        if (!scrapRepository.existsByUserUserIdAndRecipeRecipeId(userId, recipeId)) {
            Scrap scrap = new Scrap();
            scrap.setUser(user);
            scrap.setRecipe(recipe);
            scrapRepository.save(scrap);
        }
    }

    @Transactional
    @Override
    public void removeScrap(Long userId, Long recipeId) {
        scrapRepository.deleteByUserUserIdAndRecipeRecipeId(userId, recipeId);
    }

    @Override
    public List<Scrap> getScrappedRecipes(Long userId) {
        return scrapRepository.findByUserUserId(userId);
    }
}
