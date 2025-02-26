package recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import recipe.entity.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long>{
	List<Recipe> findByTitleContaining(String keyword);
	
	// 일반 유저는 삭제되지 않은 레시피만 조회 가능
    List<Recipe> findByDeletedYnFalse();

    // 관리자만 모든 레시피(삭제된 것 포함) 조회 가능
    List<Recipe> findAll();
    
    // 제목, 재료로 레시피 검색 (삭제되지 않은 데이터만)
    List<Recipe> findByTitleContainingAndDeletedYnFalse(String title);
    List<Recipe> findByIngredientsContainingAndDeletedYnFalse(String ingredient);

    List<Recipe> findTop3ByOrderByFavoriteCountDesc();

    // 필터링 선택 없이 전체 검색
    @Query("SELECT DISTINCT r FROM Recipe r " +
            "LEFT JOIN r.recipeSteps rs " +
            "WHERE r.deletedYn = false " +
            "AND (LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.ingredients) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(rs.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
     List<Recipe> searchByKeyword(@Param("keyword") String keyword);
    
    // 필터링(밥,국,후식 등) 선택 후 전체 검색
    @Query("SELECT DISTINCT r FROM Recipe r " +
            "LEFT JOIN r.recipeSteps rs " +
            "WHERE r.deletedYn = false " +
            "AND r.category = :category " +
            "AND (LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(r.ingredients) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(rs.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
     List<Recipe> searchByCategorAndKeyword(@Param("category") String category, @Param("keyword") String keyword);
    
    // 카테고리 필터링 후 요리 이름으로 검색
    @Query("SELECT DISTINCT r FROM Recipe r " +
            "LEFT JOIN r.recipeSteps rs " +
            "WHERE r.deletedYn = false " +
            "AND r.category = :category " +
            "AND LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Recipe> searchByCategoryAndTitle(@Param("category") String category, @Param("keyword") String keyword); 
    
    // 카테고리 필터링 후 재료로 검색
    @Query("SELECT DISTINCT r FROM Recipe r " +
            "LEFT JOIN r.recipeSteps rs " +
            "WHERE r.deletedYn = false " +
            "AND r.category = :category " +
            "AND LOWER(r.ingredients) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Recipe> searchByCategoryAndIngredients(@Param("category") String category, @Param("keyword") String keyword);


    
    
    
}