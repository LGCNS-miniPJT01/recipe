package recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipe.entity.Favorite;
import recipe.entity.Recipe;
import recipe.entity.User;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByRecipeAndFavoriteOwner(Recipe recipe, User favoriteOwner);
    int countByRecipe(Recipe recipe);
    List<Favorite> findByRecipe(Recipe recipe);
}
