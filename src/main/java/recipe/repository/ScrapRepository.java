package recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipe.entity.Scrap;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    List<Scrap> findByUserUserId(Long userId);
    boolean existsByUserUserIdAndRecipeRecipeId(Long userId, Long recipeId);
    void deleteByUserUserIdAndRecipeRecipeId(Long userId, Long recipeId);
}
