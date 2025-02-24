package recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import recipe.entity.Report;
import recipe.entity.Report.ReportStatus;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByRecipe_RecipeId(Long recipeId);
	List<Report> findByStatus(ReportStatus status);
}
