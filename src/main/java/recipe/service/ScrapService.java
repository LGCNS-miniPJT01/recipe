package recipe.service;

import recipe.entity.Scrap;
import java.util.List;

public interface ScrapService {
    void addScrap(Long userId, Long recipeId);
    void removeScrap(Long userId, Long recipeId);
    List<Scrap> getScrappedRecipes(Long userId);
}
