package recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import recipe.entity.Report;
import recipe.entity.Report.ReportStatus;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByRecipeId(Long recipeId);
	List<Report> findByStatus(ReportStatus status);
}
