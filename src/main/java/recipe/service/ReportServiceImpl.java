package recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import recipe.entity.Recipe;
import recipe.entity.Report;
import recipe.repository.RecipeRepository;
import recipe.repository.ReportRepository;

@Service
public class ReportServiceImpl implements ReportService{

	@Autowired
    private ReportRepository reportRepository;
	
	@Autowired
	private RecipeRepository recipeRepository;

	//레시피 신고
    @Transactional
    public void reportRecipe(Long recipeId, String reason) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        Report report = new Report();
        report.setUser(recipe.getUser());
        report.setRecipe(recipe);
        report.setReason(reason);
        report.setStatus(Report.ReportStatus.PENDING);

        reportRepository.save(report);

    }
}
