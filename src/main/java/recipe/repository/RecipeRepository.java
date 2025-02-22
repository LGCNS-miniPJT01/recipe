package recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import recipe.entity.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>{
	List<Recipe> findByTitleContaining(String keyword);
	
	// 일반 유저는 삭제되지 않은 레시피만 조회 가능
    List<Recipe> findByDeletedYnFalse();

    // 관리자만 모든 레시피(삭제된 것 포함) 조회 가능
    List<Recipe> findAll();
}