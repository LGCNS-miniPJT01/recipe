package recipe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import recipe.entity.Favorite;
import recipe.entity.Recipe;
import recipe.entity.User;
import recipe.repository.FavoriteRepository;
import recipe.repository.RecipeRepository;
import recipe.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void addFavorite(Long recipeId, Long userId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (favoriteRepository.findByRecipeAndFavoriteOwner(recipe, user).isPresent()) {
            throw new RuntimeException("Already liked this recipe");
        }

        Favorite favorite = new Favorite();
        favorite.setRecipe(recipe);
        favorite.setFavoriteOwner(user);
        favoriteRepository.save(favorite);
    }

    @Override
    @Transactional
    public void removeFavorite(Long recipeId, Long userId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Favorite favorite = favoriteRepository.findByRecipeAndFavoriteOwner(recipe, user)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));

        favoriteRepository.delete(favorite);
    }

    @Override
    public int getFavoriteCount(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
        return favoriteRepository.countByRecipe(recipe);
    }

    @Override
    public List<User> getFavoriteUsers(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        return favoriteRepository.findByRecipe(recipe)
                .stream()
                .map(Favorite::getFavoriteOwner)
                .collect(Collectors.toList());
    }
}
