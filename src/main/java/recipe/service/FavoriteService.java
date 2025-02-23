package recipe.service;

import recipe.entity.User;
import java.util.List;

public interface FavoriteService {
    void addFavorite(Long recipeId, Long userId);
    void removeFavorite(Long recipeId, Long userId);
    int getFavoriteCount(Long recipeId);
    List<User> getFavoriteUsers(Long recipeId);
}
