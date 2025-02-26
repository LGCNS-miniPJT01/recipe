package recipe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import recipe.entity.Favorite;
import recipe.entity.Recipe;
import recipe.entity.User;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByRecipeAndFavoriteOwner(Recipe recipe, User favoriteOwner);
    int countByRecipe(Recipe recipe);
    List<Favorite> findByRecipe(Recipe recipe);
    
    @Query("SELECT DISTINCT f.recipe, COUNT(f) AS favoriteCount " +
            "FROM Favorite f " +
            "GROUP BY f.recipe " +
            "ORDER BY favoriteCount DESC")
     List<Object[]> findTopRecipesByFavoriteCount();
}
